package pl.cube.planning_poker.game

import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.websocket.close
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.cube.planning_poker.Logger
import pl.cube.planning_poker.models.ProtocolJson
import pl.cube.planning_poker.models.server.*
import java.util.concurrent.ConcurrentHashMap

class Game {
    private val state = MutableStateFlow(TableState())
    private val message = MutableStateFlow<MessageState>(MessageState.NoOp)
    private val playersSockets = ConcurrentHashMap<String, WebSocketServerSession>()
    private val privateVotes = ConcurrentHashMap<String, PlanningCard>()
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        state.onEach(::broadcastGameState).launchIn(gameScope)
        message.onEach(::broadcast).launchIn(gameScope)
    }

    fun connectPlayer(
        playerId: String,
        playerName: String,
        tableId: String,
        session: WebSocketServerSession,
    ) {
        if (tableId !in KnownTables.publicTableIds) {
            Logger.d("player not connected, table not found")
            gameScope.launch {
                session.send(
                    encodeServerMessage(
                        ServerError(
                            message = "table not found",
                            code = ServerErrorCode.TableNotFound,
                        )
                    )
                )
                session.close()
            }
            return
        }

        val isNameTaken = state.value.players.any { it.name == playerName }
        if (isNameTaken) {
            Logger.d("player not connected, sending error")
            gameScope.launch {
                session.send(
                    encodeServerMessage(
                        ServerError(
                            message = "user name already taken",
                            code = ServerErrorCode.UserNameTaken,
                        )
                    )
                )
                session.close()
            }
        } else {
            playersSockets[playerId] = session
            state.update { current ->
                val nextHostId = current.hostId ?: playerId
                val nextPlayers = normalizeRoles(
                    hostId = nextHostId,
                    players = current.players + PlayerState(
                        playerId = playerId,
                        name = playerName,
                    ),
                )
                current.copy(
                    tableId = tableId,
                    revision = current.revision + 1,
                    hostId = nextHostId,
                    players = nextPlayers,
                )
            }
            Logger.d("player connected, state updated")
        }
    }

    fun disconnectPlayer(playerId: String) {
        gameScope.launch {
            playersSockets.entries.firstOrNull { it.key == playerId }?.value?.close()
        }
        playersSockets.remove(playerId)
        privateVotes.remove(playerId)
        state.update { state ->
            val nextPlayers = state.players.filterNot { it.playerId == playerId }
            val nextHostId = if (state.hostId == playerId) {
                nextPlayers.firstOrNull()?.playerId
            } else {
                state.hostId
            }
            state.copy(
                revision = state.revision + 1,
                hostId = nextHostId,
                players = normalizeRoles(nextHostId, nextPlayers),
            )
        }
    }

    fun submitVote(
        playerId: String,
        card: PlanningCard,
    ) {
        val currentState = state.value
        if (currentState.players.none { it.playerId == playerId }) {
            Logger.d("ignoring vote from unknown player")
            return
        }
        if (currentState.round.status != RoundStatus.Voting) {
            Logger.d("ignoring vote outside voting round")
            return
        }

        privateVotes[playerId] = card
        state.update { current ->
            current.copy(
                revision = current.revision + 1,
                players = current.players.map { player ->
                    if (player.playerId == playerId) {
                        player.copy(vote = PublicVoteState.VotedHidden)
                    } else {
                        player
                    }
                }
            )
        }
    }

    fun revealCards(playerId: String) {
        val currentState = state.value
        if (!canExecuteRoundAction(playerId, currentState.revealPermission, currentState)) {
            Logger.d("ignoring reveal from unauthorized player")
            return
        }
        if (currentState.round.status == RoundStatus.Revealed) {
            Logger.d("ignoring reveal on revealed round")
            return
        }

        state.update { current ->
            current.copy(
                revision = current.revision + 1,
                round = current.round.copy(status = RoundStatus.Revealed),
                players = current.players.map { player ->
                    val vote = privateVotes[player.playerId]
                    player.copy(
                        vote = if (vote == null) {
                            PublicVoteState.NotVoted
                        } else {
                            PublicVoteState.Revealed(vote)
                        }
                    )
                }
            )
        }
    }

    fun resetRound(playerId: String) {
        val currentState = state.value
        if (!canExecuteRoundAction(playerId, currentState.resetPermission, currentState)) {
            Logger.d("ignoring reset from unauthorized player")
            return
        }

        privateVotes.clear()
        state.update { current ->
            current.copy(
                revision = current.revision + 1,
                round = current.round.copy(
                    number = current.round.number + 1,
                    status = RoundStatus.Voting,
                    selfVote = null,
                ),
                players = current.players.map { player ->
                    player.copy(vote = PublicVoteState.NotVoted)
                }
            )
        }
    }

    suspend fun broadcastGameState(broadcastMessage: TableState) {
        Logger.d("broadcasting game state to all players")
        playersSockets.forEach { (playerId, socket) ->
            socket.send(
                encodeServerMessage(
                    broadcastMessage.copy(
                        selfPlayerId = playerId,
                        round = broadcastMessage.round.copy(
                            selfVote = privateVotes[playerId]
                        )
                    )
                )
            )
        }
    }

    suspend fun broadcast(broadcastMessage: MessageState) {
        when (broadcastMessage) {
            is MessageState.SingleMessage -> {
                Logger.d("broadcasting message to one player")
                playersSockets
                    .entries
                    .firstOrNull { it.key == broadcastMessage.playerId }
                    ?.value?.send(encodeServerMessage(broadcastMessage.message))
            }

            is MessageState.MultiMessage -> {
                Logger.d("broadcasting message to all players")
                playersSockets.values.forEach { socket ->
                    socket.send(encodeServerMessage(broadcastMessage.message))
                }
            }

            MessageState.NoOp -> { /* no-op */ }
        }
        message.value = MessageState.NoOp
    }

    private fun normalizeRoles(
        hostId: String?,
        players: List<PlayerState>,
    ): List<PlayerState> = players.map { player ->
        player.copy(
            role = if (player.playerId == hostId) {
                PlayerRole.Host
            } else {
                PlayerRole.Participant
            }
        )
    }

    private fun canExecuteRoundAction(
        playerId: String,
        permission: TableActionPermission,
        currentState: TableState,
    ): Boolean {
        if (currentState.players.none { it.playerId == playerId }) {
            return false
        }

        return when (permission) {
            TableActionPermission.HostOnly -> currentState.hostId == playerId
            TableActionPermission.Anyone -> true
        }
    }

    private fun encodeServerMessage(message: ServerMessage): String =
        ProtocolJson.instance.encodeToString<ServerMessage>(message)
}

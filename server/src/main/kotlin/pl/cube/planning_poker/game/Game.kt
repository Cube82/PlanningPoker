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
import kotlinx.serialization.json.Json
import pl.cube.planning_poker.Logger
import pl.cube.planning_poker.models.server.*
import java.util.concurrent.ConcurrentHashMap

class Game {
    private val state = MutableStateFlow(GameState())
    private val message = MutableStateFlow<MessageState>(MessageState.NoOp)
    private val playersSockets = ConcurrentHashMap<String, WebSocketServerSession>()
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
        val isNameTaken = state.value.players.any { it.name == playerName }
        if (isNameTaken) {
            Logger.d("player not connected, sending error")
            gameScope.launch {
                session.send(
                    Json.encodeToString(
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
            state.update {
                it.copy(
                    players = state.value.players + Player(playerName, playerId, tableId)
                )
            }
            message.value = MessageState.SingleMessage(PlayerJoined(playerId), playerId)
            Logger.d("player connected, state updated")
        }
    }

    fun disconnectPlayer(playerId: String) {
        gameScope.launch {
            playersSockets.entries.firstOrNull { it.key == playerId }?.value?.close()
        }
        playersSockets.remove(playerId)
        state.update { state ->
            state.copy(
                players = state.players.filterNot { it.playerId == playerId }
            )
        }
    }

    suspend fun broadcastGameState(broadcastMessage: ServerMessage) {
        Logger.d("broadcasting game state to all players")
        playersSockets.values.forEach { socket ->
            socket.send(Json.encodeToString(broadcastMessage))
        }
    }

    suspend fun broadcast(broadcastMessage: MessageState) {
        when (broadcastMessage) {
            is MessageState.SingleMessage -> {
                Logger.d("broadcasting message to one player")
                playersSockets
                    .entries
                    .firstOrNull { it.key == broadcastMessage.playerId }
                    ?.value?.send(Json.encodeToString(broadcastMessage.message))
            }

            is MessageState.MultiMessage -> {
                Logger.d("broadcasting message to all players")
                playersSockets.values.forEach { socket ->
                    socket.send(Json.encodeToString(broadcastMessage.message))
                }
            }

            MessageState.NoOp -> { /* no-op */ }
        }
        message.value = MessageState.NoOp
    }
}

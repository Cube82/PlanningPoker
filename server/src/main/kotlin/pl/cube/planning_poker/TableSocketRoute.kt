package pl.cube.planning_poker

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import pl.cube.planning_poker.game.Game
import pl.cube.planning_poker.models.client.ClientMessage
import pl.cube.planning_poker.models.client.JoinTable
import pl.cube.planning_poker.models.client.ResetRound
import pl.cube.planning_poker.models.client.RevealCards
import pl.cube.planning_poker.models.client.SelectCard
import pl.cube.planning_poker.models.client.UnselectCard
import pl.cube.planning_poker.models.ProtocolJson
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Route.tableSocket(game: Game) {
    route("/table") {
        webSocket {
            val playerId = Uuid.random().toString()
            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val serializedMessage = frame.readText()
                        Logger.d("received: $serializedMessage")
                        val message = ProtocolJson.instance.decodeFromString<ClientMessage>(serializedMessage)
                        when (message) {
                            is JoinTable -> {
                                Logger.d("user ${message.userName} connecting")
                                game.connectPlayer(playerId, message.userName, message.tableId, this)
                            }

                            is SelectCard -> {
                                Logger.d("user $playerId voting")
                                game.submitVote(playerId, message.card)
                            }

                            UnselectCard -> {
                                Logger.d("user $playerId removing vote")
                                game.removeVote(playerId)
                            }

                            RevealCards -> {
                                Logger.d("user $playerId revealing")
                                game.revealCards(playerId)
                            }

                            ResetRound -> {
                                Logger.d("user $playerId resetting round")
                                game.resetRound(playerId)
                            }

                            else -> {
                                Logger.d("got other message")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.e(e)
            } finally {
                Logger.d("disconnecting player $playerId")
                game.disconnectPlayer(playerId)
            }
        }
    }
}

package pl.cube.planning_poker

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import pl.cube.planning_poker.game.Game
import pl.cube.planning_poker.models.client.ClientMessage
import pl.cube.planning_poker.models.client.JoinTable
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
                        val message = Json.decodeFromString<ClientMessage>(serializedMessage)
                        if (message is JoinTable) {
                            Logger.d("user ${message.userName} connecting")
                            game.connectPlayer(playerId, message.userName, message.tableId, this)
                        } else {
                            Logger.d("got other message")
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

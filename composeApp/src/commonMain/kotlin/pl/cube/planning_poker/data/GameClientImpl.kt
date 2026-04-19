package pl.cube.planning_poker.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import pl.cube.planning_poker.logger.Logger
import pl.cube.planning_poker.models.client.ClientMessage
import pl.cube.planning_poker.models.server.ServerMessage
import pl.cube.planning_poker.net.BackendConfig

class GameClientImpl(
    private val client: HttpClient,
): GameClient {

    private var session: WebSocketSession? = null
    private val sessionMutex = Mutex()

    override fun getTableMessageStream(): Flow<ServerMessage> {
        return flow {
            val currentSession = ensureSession()
            val messages = currentSession
                .incoming
                .consumeAsFlow()
                .mapNotNull { frame ->
                    Logger.d("received frame")
                    when (frame) {
                        is Frame.Close -> {
                            Logger.d("close ${frame.readReason()}")
                            return@mapNotNull null
                        }
                        is Frame.Text -> {
                            Logger.d("got ${frame.readText()}")
                            Json.decodeFromString<ServerMessage>(frame.readText())
                        }
                        else -> {
                            Logger.e("wrong frame type")
                            return@mapNotNull null
                        }
                    }

                }

            try {
                emitAll(messages)
            } finally {
                sessionMutex.withLock {
                    if (session == currentSession) {
                        session = null
                    }
                }
            }
        }
    }

    override suspend fun sendMessage(message: ClientMessage) {
        ensureSession()
            .outgoing
            .trySend(Frame.Text(Json.encodeToString(message)))
            .onClosed {
                Logger.d("Can not send: the channel is closed")
                sessionMutex.withLock {
                    session = null
                }
            }
    }

    override suspend fun close() {
        Logger.d("closing session")
        session?.close()
        session = null
    }

    private suspend fun ensureSession(): WebSocketSession = sessionMutex.withLock {
        session ?: client.webSocketSession {
            url(BackendConfig.webSocketUrl)
        }.also { createdSession ->
            session = createdSession
        }
    }
}

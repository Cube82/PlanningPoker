package pl.cube.planning_poker.data

import kotlinx.coroutines.flow.Flow
import pl.cube.planning_poker.models.client.ClientMessage
import pl.cube.planning_poker.models.server.ServerMessage

interface GameClient {
    fun getTableMessageStream(): Flow<ServerMessage>
    suspend fun sendMessage(message: ClientMessage)
    suspend fun close()
}

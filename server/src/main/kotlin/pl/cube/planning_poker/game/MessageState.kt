package pl.cube.planning_poker.game

import pl.cube.planning_poker.models.server.ServerMessage

sealed class MessageState {
    data class SingleMessage(val message: ServerMessage, val playerId: String) : MessageState()
    data class MultiMessage(val message: ServerMessage) : MessageState()
    data object NoOp : MessageState()
}

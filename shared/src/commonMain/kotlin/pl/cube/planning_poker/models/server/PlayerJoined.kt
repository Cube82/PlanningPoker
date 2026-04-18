package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("playerJoined")
data class PlayerJoined(
    val playerId: String,
) : ServerMessage

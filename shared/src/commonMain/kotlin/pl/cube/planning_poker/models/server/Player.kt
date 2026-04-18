package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("player")
data class Player(
    val name: String,
    val playerId: String,
    val tableId: String,
)
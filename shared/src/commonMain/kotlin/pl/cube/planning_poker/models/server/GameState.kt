package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("gameState")
data class GameState(
    @SerialName("players")
    val players: List<Player> = emptyList(),
) : ServerMessage
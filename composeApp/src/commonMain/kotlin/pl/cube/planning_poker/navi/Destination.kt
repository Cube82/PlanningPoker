package pl.cube.planning_poker.navi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface Destination {
    @Serializable
    @SerialName("lobby")
    data class Lobby(
        val player: String? = null,
        val tableId: String? = null,
    ) : Destination

    @Serializable
    @SerialName("table")
    data class Table(
        val tableId: String,
        val player: String? = null,
    ) : Destination
}

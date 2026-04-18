package pl.cube.planning_poker.navi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface Destination {
    @Serializable
    @SerialName("lobby")
    data object Lobby : Destination

    @Serializable
    @SerialName("table")
    data class Table(val player: String) : Destination
}

package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tableState")
data class TableState(
    @SerialName("tableId")
    val tableId: String = "",
    @SerialName("revision")
    val revision: Long = 0,
    @SerialName("selfPlayerId")
    val selfPlayerId: String? = null,
    @SerialName("hostId")
    val hostId: String? = null,
    @SerialName("revealPermission")
    val revealPermission: TableActionPermission = TableActionPermission.HostOnly,
    @SerialName("resetPermission")
    val resetPermission: TableActionPermission = TableActionPermission.HostOnly,
    @SerialName("deck")
    val deck: List<PlanningCard> = PlanningDecks.Fibonacci.cards,
    @SerialName("round")
    val round: RoundState = RoundState(),
    @SerialName("players")
    val players: List<PlayerState> = emptyList(),
) : ServerMessage

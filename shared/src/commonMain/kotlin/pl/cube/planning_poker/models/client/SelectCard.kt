package pl.cube.planning_poker.models.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.cube.planning_poker.models.server.PlanningCard

@Serializable
@SerialName("selectCard")
data class SelectCard(
    val card: PlanningCard,
) : ClientMessage

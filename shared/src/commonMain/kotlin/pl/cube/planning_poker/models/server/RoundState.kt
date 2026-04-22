package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoundState(
    @SerialName("number")
    val number: Int = 1,
    @SerialName("status")
    val status: RoundStatus = RoundStatus.Voting,
    @SerialName("selfVote")
    val selfVote: PlanningCard? = null,
)

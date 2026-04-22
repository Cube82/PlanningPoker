package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RoundStatus {
    @SerialName("voting")
    Voting,

    @SerialName("revealed")
    Revealed,
}

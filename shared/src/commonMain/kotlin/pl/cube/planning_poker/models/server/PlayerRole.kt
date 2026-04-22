package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlayerRole {
    @SerialName("host")
    Host,

    @SerialName("participant")
    Participant,
}

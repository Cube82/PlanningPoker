package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerState(
    @SerialName("playerId")
    val playerId: String,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: PlayerRole = PlayerRole.Participant,
    @SerialName("vote")
    val vote: PublicVoteState = PublicVoteState.NotVoted,
)

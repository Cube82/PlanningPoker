package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PublicVoteState {
    @Serializable
    @SerialName("notVoted")
    data object NotVoted : PublicVoteState

    @Serializable
    @SerialName("votedHidden")
    data object VotedHidden : PublicVoteState

    @Serializable
    @SerialName("missedVote")
    data object MissedVote : PublicVoteState

    @Serializable
    @SerialName("revealed")
    data class Revealed(
        val card: PlanningCard,
    ) : PublicVoteState
}

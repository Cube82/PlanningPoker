package pl.cube.planning_poker.features.table.view

import pl.cube.planning_poker.features.table.viewmodel.ConnectionState
import pl.cube.planning_poker.features.table.viewmodel.TableUiState
import pl.cube.planning_poker.models.server.*

internal const val SELF_PLAYER_ID = "999"

internal object TablePreviewFixtures {
    val uiState = TableUiState(
        playerName = "John Doe",
        tableName = "main",
        connectionState = ConnectionState.Joined,
    )

    val tableStateVoting = TableState(
        selfPlayerId = SELF_PLAYER_ID,
        players = listOf(
            PlayerState(
                playerId = SELF_PLAYER_ID,
                name = "John Doe",
                vote = PublicVoteState.VotedHidden,
            ),
            PlayerState(
                playerId = "2",
                name = "Awesome Player",
                vote = PublicVoteState.VotedHidden,
            ),
            PlayerState(
                playerId = "3",
                name = "yet another player",
                role = PlayerRole.Host,
            ),
        ),
        round = RoundState(selfVote = PlanningCard.Three),
    )

    val tableStateRevealed = TableState(
        selfPlayerId = SELF_PLAYER_ID,
        hostId = SELF_PLAYER_ID,
        players = listOf(
            PlayerState(
                playerId = SELF_PLAYER_ID,
                name = "John Doe",
                vote = PublicVoteState.Revealed(PlanningCard.Five),
                role = PlayerRole.Host,
            ),
            PlayerState(
                playerId = "2",
                name = "Awesome Player",
                vote = PublicVoteState.Revealed(PlanningCard.Three),
            ),
            PlayerState(
                playerId = "3",
                name = "yet another player",
                vote = PublicVoteState.Revealed(PlanningCard.Three),
            ),
        ),
        round = RoundState(status = RoundStatus.Revealed),
    )

    val players = listOf(
        PlayerState(
            playerId = SELF_PLAYER_ID,
            name = "John Doe",
            vote = PublicVoteState.Revealed(card = PlanningCard.OneHundred),
        ),
        PlayerState(
            playerId = "123",
            name = "Awesome Player",
            vote = PublicVoteState.MissedVote,
        ),
        PlayerState(
            playerId = "456",
            name = "Anna Boo Boo",
            role = PlayerRole.Host,
        ),
        PlayerState(
            playerId = "789",
            name = "SilverBullet",
            vote = PublicVoteState.VotedHidden,
        ),
    )
}
package pl.cube.planning_poker.features.table.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.cube.planning_poker.models.server.PlayerRole
import pl.cube.planning_poker.models.server.PlayerState
import pl.cube.planning_poker.models.server.PublicVoteState
import pl.cube.planning_poker.ui.DefaultPreview
import pl.cube.planning_poker.ui.components.AppCard
import pl.cube.planning_poker.ui.components.AppDivider
import pl.cube.planning_poker.ui.components.AppText
import pl.cube.planning_poker.ui.dimen4
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.table_current_player
import planningpoker.composeapp.generated.resources.table_player_is_host
import kotlin.math.absoluteValue

private const val UNKNOWN_INITIALS = "?"
private val avatarSize = 50.dp

@Composable
internal fun PlayersList(
    players: List<PlayerState>,
    selfPlayerId: String?,
) {
    AppCard {
        players.forEachIndexed { index, player ->
            val isCurrentPlayer = player.playerId == selfPlayerId
            PlayersListItem(player, isCurrentPlayer)
            if (index != players.lastIndex) {
                AppDivider()
            }
        }
    }
}

@Composable
private fun PlayersListItem(
    player: PlayerState,
    isCurrentPlayer: Boolean,
) {
    val supportingContent = getAdditionalPlayerData(player.role, isCurrentPlayer)
    ListItem(
        modifier = Modifier.padding(top = dimen4, bottom = dimen4),
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppText(player.name)
            }
        },
        supportingContent = {
            if (supportingContent.isNotEmpty()) {
                AppText(supportingContent)
            }
        },
        leadingContent = {
            CircleInitialAvatar(name = player.name)
        },
        trailingContent = {
            VoteBadge(player.vote)
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
fun getAdditionalPlayerData(
    role: PlayerRole,
    currentPlayer: Boolean,
): String {
    val elements = buildList {
        if (currentPlayer) {
            add(stringResource(Res.string.table_current_player))
        }
        if (role == PlayerRole.Host) {
            add(stringResource(Res.string.table_player_is_host))
        }
    }

    return elements.joinToString(" - ")
}

@Composable
private fun VoteBadge(vote: PublicVoteState) {
    when (vote) {
        PublicVoteState.NotVoted -> NotVotedCardHidden()

        PublicVoteState.VotedHidden -> VotedCardHidden()

        PublicVoteState.MissedVote -> NotVotedCardRevealed()

        is PublicVoteState.Revealed -> VotedCardRevealed(vote.card)
    }
}

@Composable
fun CircleInitialAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = avatarSize,
) {
    val backgroundColor = getColorFromName(name)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        AppText(
            text = name.initials(),
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun String.initials(): String {
    val parts = trim()
        .split(" ")
        .filter { it.isNotEmpty() }

    return when {
        parts.isEmpty() -> UNKNOWN_INITIALS
        parts.size == 1 -> parts[0].first().uppercase()
        else -> "${parts[0].first()}${parts[1].first()}".uppercase()
    }
}

private fun getColorFromName(name: String): Color {
    val colors = listOf(
        Color(0xFFB3E5FC), // Light Blue
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFCCBC), // Light Orange
        Color(0xFFD1C4E9), // Light Purple
        Color(0xFFFFCDD2)  // Light Red
    )

    val index = (name.hashCode().absoluteValue) % colors.size
    return colors[index]
}

@PreviewLightDark
@Composable
private fun PlayersListPreview() {

    DefaultPreview {
        Column {
            PlayersList(TablePreviewFixtures.players, SELF_PLAYER_ID)
        }
    }
}

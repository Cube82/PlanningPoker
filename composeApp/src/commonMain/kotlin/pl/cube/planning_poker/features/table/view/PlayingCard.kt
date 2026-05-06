package pl.cube.planning_poker.features.table.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.cube.planning_poker.models.server.PlanningCard
import pl.cube.planning_poker.models.server.displayLabel
import pl.cube.planning_poker.ui.DefaultPreview
import pl.cube.planning_poker.ui.components.AppText
import pl.cube.planning_poker.ui.dimen1
import pl.cube.planning_poker.ui.dimen2
import pl.cube.planning_poker.ui.dimen8
import pl.cube.planning_poker.ui.theme.AppThemeExtras
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.check_24px
import planningpoker.composeapp.generated.resources.more_horiz_24px

private val bigCardCorner = 12.dp
private val bigCardHeight = 100.dp
private val bigCardPadding = 6.dp
private val bigCardWidth = 72.dp
private val smallCardCorner = 6.dp
private val smallCardHeight = 50.dp
private val smallCardWidth = 36.dp

@Composable
internal fun VotedCardRevealed(
    card: PlanningCard,
) {
    SmallCard(
        color = AppThemeExtras.colors.cardBackground,
        contentColor = AppThemeExtras.colors.cardContent,
        content = SmallCardContent.Card(card),
    )
}

@Composable
internal fun NotVotedCardRevealed() {
    SmallCard(
        color = AppThemeExtras.colors.cardDisabledBackground,
        contentColor = AppThemeExtras.colors.cardDisabledContent,
        content = SmallCardContent.Icon(Res.drawable.more_horiz_24px),
    )
}

@Composable
internal fun NotVotedCardHidden() {
    SmallCard(
        color = AppThemeExtras.colors.badgeNotVoted,
        contentColor = AppThemeExtras.colors.onBadgeNotVoted,
        content = SmallCardContent.Icon(Res.drawable.more_horiz_24px),
    )
}

@Composable
internal fun VotedCardHidden() {
    SmallCard(
        color = AppThemeExtras.colors.badgeVoted,
        contentColor = AppThemeExtras.colors.onBadgeVoted,
        content = SmallCardContent.Icon(Res.drawable.check_24px),
    )
}

@Composable
private fun SmallCard(
    color: Color,
    contentColor: Color,
    content: SmallCardContent,
) {
    Surface(
        color = color,
        contentColor = contentColor,
        border = BorderStroke(
            width = dimen1,
            color = AppThemeExtras.colors.cardBorder,
        ),
        shape = RoundedCornerShape(smallCardCorner),
    ) {
        Box(
            modifier = Modifier
                .width(smallCardWidth)
                .height(smallCardHeight),
            contentAlignment = Alignment.Center,
        ) {
            when (content) {
                is SmallCardContent.Card -> {
                    AppText(
                        text = content.card.displayLabel(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                is SmallCardContent.Icon -> {
                    Icon(
                        painter = painterResource(content.icon),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

private sealed interface SmallCardContent {
    data class Card(val card: PlanningCard) : SmallCardContent
    data class Icon(val icon: DrawableResource) : SmallCardContent
}

@Composable
internal fun BigCard(
    card: PlanningCard,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val pressed by interactionSource.collectIsPressedAsState()
    val colors = bigCardColors(
        enabled = enabled,
        selected = selected,
        hovered = hovered,
        focused = focused,
        pressed = pressed,
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.semantics {
            this.selected = selected
        },
        enabled = enabled,
        color = colors.container,
        contentColor = colors.content,
        border = BorderStroke(
            width = if (selected || focused) dimen2 else dimen1,
            color = colors.border,
        ),
        shadowElevation = if (selected || hovered || focused) dimen2 else 0.dp,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(bigCardCorner),
    ) {
        Column(
            modifier = Modifier
                .width(bigCardWidth)
                .height(bigCardHeight)
                .padding(bigCardPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AppText(
                text = card.displayLabel(),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
            )
            AppText(
                text = card.displayLabel(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            AppText(
                text = card.displayLabel(),
                modifier = Modifier.fillMaxWidth().rotate(180f),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun bigCardColors(
    enabled: Boolean,
    selected: Boolean,
    hovered: Boolean,
    focused: Boolean,
    pressed: Boolean,
): PlayingCardColors {
    val appColors = AppThemeExtras.colors

    return when {
        !enabled -> PlayingCardColors(
            container = appColors.cardDisabledBackground,
            content = appColors.cardDisabledContent,
            border = appColors.cardDisabledBorder,
        )

        selected -> PlayingCardColors(
            container = appColors.cardSelectedBackground,
            content = appColors.cardSelectedContent,
            border = appColors.cardSelectedBorder,
        )

        pressed -> PlayingCardColors(
            container = appColors.cardPressedBackground,
            content = appColors.cardPressedContent,
            border = appColors.cardPressedBorder,
        )

        hovered || focused -> PlayingCardColors(
            container = appColors.cardFocusedBackground,
            content = appColors.cardFocusedContent,
            border = appColors.cardFocusedBorder,
        )

        else -> PlayingCardColors(
            container = appColors.cardBackground,
            content = appColors.cardContent,
            border = appColors.cardBorder,
        )
    }
}

private data class PlayingCardColors(
    val container: Color,
    val content: Color,
    val border: Color,
)

@PreviewLightDark
@Composable
private fun SmallCardsPreview() {
    DefaultPreview {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimen8)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimen8),
            ) {
                VotedCardRevealed(PlanningCard.Zero)
                VotedCardRevealed(PlanningCard.Half)
                VotedCardRevealed(PlanningCard.Three)
                VotedCardRevealed(PlanningCard.OneHundred)
                VotedCardRevealed(PlanningCard.Question)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimen8),
            ) {
                NotVotedCardRevealed()
                NotVotedCardHidden()
                VotedCardHidden()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BigCardsPreview() {
    DefaultPreview {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dimen8),
            verticalArrangement = Arrangement.spacedBy(dimen8),
        ) {
            BigCard(PlanningCard.Zero)
            BigCard(PlanningCard.Half, selected = true)
            BigCard(PlanningCard.Three, enabled = false)
            BigCard(PlanningCard.OneHundred)
            BigCard(PlanningCard.Question)
        }
    }
}

package pl.cube.planning_poker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppCustomColors(
    val textFieldContainer: Color,
    val textFieldBorder: Color,
    val textFieldPlaceholder: Color,
    val badgeVoted: Color,
    val onBadgeVoted: Color,
    val badgeNotVoted: Color,
    val onBadgeNotVoted: Color,
    val divider: Color,
    val cardBackground: Color,
    val cardContent: Color,
    val cardBorder: Color,
    val cardDisabledBackground: Color,
    val cardDisabledContent: Color,
    val cardDisabledBorder: Color,
    val cardSelectedBackground: Color,
    val cardSelectedContent: Color,
    val cardSelectedBorder: Color,
    val cardPressedBackground: Color,
    val cardPressedContent: Color,
    val cardPressedBorder: Color,
    val cardFocusedBackground: Color,
    val cardFocusedContent: Color,
    val cardFocusedBorder: Color,
    val logo: Color,
)

private val lightCustomColors = AppCustomColors(
    textFieldContainer = textFieldContainerLight,
    textFieldBorder = textFieldBorderLight,
    textFieldPlaceholder = textFieldPlaceholderLight,
    divider = dividerLight,
    badgeVoted = badgeVotedLight,
    onBadgeVoted = onBadgeVotedLight,
    badgeNotVoted = badgeNotVotedLight,
    onBadgeNotVoted = onBadgeNotVotedLight,
    cardBackground = Color(0xFFFFFFFF),
    cardContent = Color(0xFF104A73),
    cardBorder = Color(0xFFC6CBCF),
    cardDisabledBackground = Color(0x61FFFFFF),
    cardDisabledContent = Color(0x611A1B21),
    cardDisabledBorder = Color(0x61C5C6D0),
    cardSelectedBackground = Color(0xFFD9E2FF),
    cardSelectedContent = Color(0xFF2F4578),
    cardSelectedBorder = Color(0xFF475D92),
    cardPressedBackground = Color(0xFFDAE2FF),
    cardPressedContent = Color(0xFF304578),
    cardPressedBorder = Color(0xFF485D92),
    cardFocusedBackground = Color(0xFFFFFFFF),
    cardFocusedContent = Color(0xFF104A73),
    cardFocusedBorder = Color(0xFF475D92),
    logo = Color(0xFF1D3A6E),
)

private val darkCustomColors = AppCustomColors(
    textFieldContainer = textFieldContainerDark,
    textFieldBorder = textFieldBorderDark,
    textFieldPlaceholder = textFieldPlaceholderDark,
    divider = dividerDark,
    badgeVoted = badgeVotedDark,
    onBadgeVoted = onBadgeVotedDark,
    badgeNotVoted = badgeNotVotedDark,
    onBadgeNotVoted = onBadgeNotVotedDark,
    cardBackground = Color(0xFF25333E),
    cardContent = Color(0xFFCEE5FF),
    cardBorder = Color(0xFF25333E),
    cardDisabledBackground = Color(0x6144464F),
    cardDisabledContent = Color(0x61E2E2E9),
    cardDisabledBorder = Color(0x6144464F),
    cardSelectedBackground = Color(0xFF2F4578),
    cardSelectedContent = Color(0xFFD9E2FF),
    cardSelectedBorder = Color(0xFF1D3A6E),
    cardPressedBackground = Color(0xFF304578),
    cardPressedContent = Color(0xFFDAE2FF),
    cardPressedBorder = Color(0xFFB1C5FF),
    cardFocusedBackground = Color(0xFF25333E),
    cardFocusedContent = Color(0xFFCEE5FF),
    cardFocusedBorder = Color(0xFF1D3A6E),
    logo = Color(0xFFF6F7F9),
)

private val LocalAppCustomColors = staticCompositionLocalOf<AppCustomColors> {
    error("No AppCustomColors provided")
}

val LocalIsDarkTheme = staticCompositionLocalOf { false }

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }
    val customColors = when {
        darkTheme -> darkCustomColors
        else -> lightCustomColors
    }

    CompositionLocalProvider(
        LocalAppCustomColors provides customColors,
        LocalIsDarkTheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography(),
            content = content
        )
    }
}

object AppThemeExtras {
    val colors: AppCustomColors
        @Composable get() = LocalAppCustomColors.current
}


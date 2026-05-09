package pl.cube.planning_poker.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.cube.planning_poker.ui.settings.AppSettingsState
import pl.cube.planning_poker.ui.settings.AppThemeMode
import planningpoker.composeapp.generated.resources.*

@Composable
fun AppSettingsMenu(
    state: AppSettingsState,
    onThemeModeChange: (AppThemeMode) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(Res.drawable.more_horiz_24px),
            contentDescription = stringResource(Res.string.settings_menu_content_description),
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        SettingsSectionHeader(text = stringResource(Res.string.settings_theme_section))
        ThemeMenuItem(
            text = Res.string.settings_theme_system,
            selected = state.themeMode == AppThemeMode.System,
            onClick = {
                onThemeModeChange(AppThemeMode.System)
                expanded = false
            },
        )
        ThemeMenuItem(
            text = Res.string.settings_theme_light,
            selected = state.themeMode == AppThemeMode.Light,
            onClick = {
                onThemeModeChange(AppThemeMode.Light)
                expanded = false
            },
        )
        ThemeMenuItem(
            text = Res.string.settings_theme_dark,
            selected = state.themeMode == AppThemeMode.Dark,
            onClick = {
                onThemeModeChange(AppThemeMode.Dark)
                expanded = false
            },
        )
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    DropdownMenuItem(
        text = {
            AppText(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelSmall,
            )
        },
        onClick = {},
        enabled = false,
    )
}

@Composable
private fun ThemeMenuItem(
    text: StringResource,
    selected: Boolean,
    onClick: () -> Unit,
) {
    SettingsMenuItem(
        text = text,
        selected = selected,
        onClick = onClick,
    )
}

@Composable
private fun SettingsMenuItem(
    text: StringResource,
    selected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { AppText(stringResource(text)) },
        onClick = onClick,
        leadingIcon = {
            if (selected) {
                Icon(
                    painter = painterResource(Res.drawable.check_24px),
                    contentDescription = null,
                )
            }
        },
    )
}

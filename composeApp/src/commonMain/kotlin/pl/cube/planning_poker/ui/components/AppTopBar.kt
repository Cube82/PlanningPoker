package pl.cube.planning_poker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import pl.cube.planning_poker.ui.dimen4
import pl.cube.planning_poker.ui.theme.LocalAppSettings
import pl.cube.planning_poker.ui.theme.LocalOnThemeModeChange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    subtitle: String,
) {
    val settingsState = LocalAppSettings.current
    val onThemeModeChange = LocalOnThemeModeChange.current

    TopAppBar(
        title = {
            Column {
                AppLogo()
                AppSpacer(dimen4)
                AppText(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        actions = {
            AppSettingsMenu(
                state = settingsState,
                onThemeModeChange = onThemeModeChange,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

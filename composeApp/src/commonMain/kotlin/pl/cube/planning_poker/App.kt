package pl.cube.planning_poker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import pl.cube.planning_poker.di.appModule
import pl.cube.planning_poker.features.lobby.view.LobbyScreen
import pl.cube.planning_poker.features.table.view.TableScreen
import pl.cube.planning_poker.navi.Destination
import pl.cube.planning_poker.navi.NavigationAction
import pl.cube.planning_poker.navi.Navigator
import pl.cube.planning_poker.navi.ObserveAsEvents
import pl.cube.planning_poker.preferences.AppSettingsRepository
import pl.cube.planning_poker.ui.dimen16
import pl.cube.planning_poker.ui.settings.AppLanguage
import pl.cube.planning_poker.ui.settings.AppSettingsState
import pl.cube.planning_poker.ui.settings.AppThemeMode
import pl.cube.planning_poker.ui.theme.AppTheme

private var isKoinStarted = false

@Composable
fun App(
    onNavHostReady: suspend (androidx.navigation.NavHostController) -> Unit = {},
) {
    initKoin()
    val settingsRepository = koinInject<AppSettingsRepository>()
    val navController = rememberNavController()
    var themeMode by remember { mutableStateOf(settingsRepository.getThemeMode()) }
    var language by remember { mutableStateOf(AppLanguage.System) }
    val settingsState = AppSettingsState(
        themeMode = themeMode,
        language = language,
    )
    val useDarkTheme = when (themeMode) {
        AppThemeMode.System -> isSystemInDarkTheme()
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }

    AppTheme(darkTheme = useDarkTheme) {
        val navigator = koinInject<Navigator>()

        LaunchedEffect(navController) {
            onNavHostReady(navController)
        }

        ObserveAsEvents(flow = navigator.navigationAction) { action ->
            when (action) {
                is NavigationAction.Navigate ->
                    navController.navigate(action.destination) { action.options(this) }

                NavigationAction.PopBackStack -> navController.popBackStack()
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(dimen16),

                ) {
                NavHost(
                    navController = navController,
                    startDestination = navigator.startDestination,
                ) {
                    composable<Destination.Lobby> {
                        LobbyScreen(
                            settingsState = settingsState,
                            onThemeModeChange = { newThemeMode ->
                                themeMode = newThemeMode
                                settingsRepository.setThemeMode(newThemeMode)
                            },
                        )
                    }
                    composable<Destination.Table> {
                        TableScreen(
                            settingsState = settingsState,
                            onThemeModeChange = { newThemeMode ->
                                themeMode = newThemeMode
                                settingsRepository.setThemeMode(newThemeMode)
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    if (isKoinStarted) {
        return
    }

    startKoin {
        appDeclaration()
        modules(appModule)
    }
    isKoinStarted = true
}

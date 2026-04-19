package pl.cube.planning_poker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import pl.cube.planning_poker.ui.dimen16
import pl.cube.planning_poker.ui.theme.AppTheme

@Composable
@Preview
fun App(
    onNavHostReady: suspend (androidx.navigation.NavHostController) -> Unit = {},
) {
    initKoin()
    val navController = rememberNavController()

    AppTheme {
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

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(dimen16),

            ) {
            NavHost(
                navController = navController,
                startDestination = navigator.startDestination,
            ) {
                composable<Destination.Lobby> {
                    LobbyScreen()
                }
                composable<Destination.Table> {
                    TableScreen()
                }
            }
        }
    }
}

private fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule)
}

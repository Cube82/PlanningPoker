package pl.cube.planning_poker.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import pl.cube.planning_poker.data.GameClientImpl
import pl.cube.planning_poker.features.lobby.viewmodel.LobbyViewModel
import pl.cube.planning_poker.features.table.viewmodel.TableViewModel
import pl.cube.planning_poker.helpers.PlayerNameValidator
import pl.cube.planning_poker.navi.ComposeNavigator
import pl.cube.planning_poker.navi.Destination
import pl.cube.planning_poker.navi.Navigator
import pl.cube.planning_poker.net.httpClient
import pl.cube.planning_poker.preferences.AppSettingsRepository

val appModule = module {
    single<Navigator> {
        ComposeNavigator(startDestination = Destination.Lobby())
    }

    single<PlayerNameValidator> { PlayerNameValidator() }
    single<AppSettingsRepository> { AppSettingsRepository() }
    single<GameClientImpl> { GameClientImpl(httpClient()) }

    viewModelOf(::LobbyViewModel)
    viewModelOf(::TableViewModel)
}

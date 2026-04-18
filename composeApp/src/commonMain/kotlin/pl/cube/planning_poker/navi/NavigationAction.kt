package pl.cube.planning_poker.navi

import androidx.navigation.NavOptionsBuilder

internal sealed interface NavigationAction {

    data class Navigate(
        val destination: Destination,
        val options: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationAction

    data object PopBackStack : NavigationAction
}
package pl.cube.planning_poker.navi

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal interface Navigator {
    val startDestination: Destination
    val navigationAction: Flow<NavigationAction>

    suspend fun navigate(
        destination: Destination,
        options: NavOptionsBuilder.() -> Unit = {},
    )

    suspend fun popBackStack()
}

internal class ComposeNavigator(
    override val startDestination: Destination,
) : Navigator {
    private val _navigationAction = Channel<NavigationAction>()
    override val navigationAction = _navigationAction.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        options: NavOptionsBuilder.() -> Unit,
    ) {
        _navigationAction.send(
            NavigationAction.Navigate(destination = destination, options = options)
        )
    }

    override suspend fun popBackStack() {
        _navigationAction.send(NavigationAction.PopBackStack)
    }
}
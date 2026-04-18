package pl.cube.planning_poker.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass
import pl.cube.planning_poker.ui.dimenWide

internal val noop = { /* no-op */}

@Composable
internal fun getWidthClass() = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

@Composable
internal fun Modifier.fillWidthWide(): Modifier {
    val width = when (getWidthClass()) {
        WindowWidthSizeClass.COMPACT -> Modifier.fillMaxWidth()
        else -> Modifier.width(dimenWide)
    }

    return this.then(width)
}
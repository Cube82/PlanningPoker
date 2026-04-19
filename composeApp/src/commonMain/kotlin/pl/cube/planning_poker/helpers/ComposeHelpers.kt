package pl.cube.planning_poker.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import pl.cube.planning_poker.ui.dimenWide

internal val noop = { /* no-op */}

@Composable
internal fun isWideWindow() =
    currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

@Composable
internal fun Modifier.fillWidthWide(): Modifier {
    val width = if (isWideWindow()) Modifier.width(dimenWide) else Modifier.fillMaxWidth()
    return this.then(width)
}
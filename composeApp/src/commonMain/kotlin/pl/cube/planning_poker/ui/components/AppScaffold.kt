package pl.cube.planning_poker.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import pl.cube.planning_poker.ui.dimen16

@Composable
fun AppScaffold(
    subtitle: String,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                subtitle = subtitle,
            )
        },
        contentWindowInsets = WindowInsets(left = dimen16, right = dimen16, bottom = dimen16),
    ) { innerPadding ->
        content(innerPadding)
    }
}
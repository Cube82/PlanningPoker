package pl.cube.planning_poker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import pl.cube.planning_poker.ui.theme.AppTheme

@Composable
fun DefaultPreview(
    padding: Dp = dimen16,
    content: @Composable (BoxScope.() -> Unit)
) {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            Box(
                modifier = Modifier.padding(padding),
            ) {
                content()
            }
        }
    }
}
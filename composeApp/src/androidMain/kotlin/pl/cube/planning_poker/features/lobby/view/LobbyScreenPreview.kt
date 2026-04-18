package pl.cube.planning_poker.features.lobby.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import pl.cube.planning_poker.preview.DefaultPreview

@PreviewLightDark
@Composable
private fun LobbyScreenPreview() {
    DefaultPreview {
        LobbyLayout(
            "",
            null,
            {},
            {},
        )
    }
}


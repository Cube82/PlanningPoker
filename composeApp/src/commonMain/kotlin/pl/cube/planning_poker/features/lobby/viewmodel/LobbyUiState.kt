package pl.cube.planning_poker.features.lobby.viewmodel

import androidx.compose.runtime.Stable
import pl.cube.planning_poker.helpers.PlayerNameValidationError

@Stable
internal data class LobbyUiState(
    val playerNameError: PlayerNameValidationError? = null,
)

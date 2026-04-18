package pl.cube.planning_poker.features.lobby.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.cube.planning_poker.helpers.PlayerNameValidator
import pl.cube.planning_poker.helpers.ValidateStatus
import pl.cube.planning_poker.navi.Destination
import pl.cube.planning_poker.navi.Navigator

internal class LobbyViewModel(
    private val playerNameValidator: PlayerNameValidator,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    var playerName by mutableStateOf("")
        private set

    fun updatePlayerName(input: String) {
        _uiState.update { it.copy(playerNameError = null) }
        playerName = input
    }

    fun joinTable() {
        playerName = playerName.trim()
        val result = playerNameValidator.isPlayerNameValid(playerName)
        if (result is ValidateStatus.Invalid) {
            _uiState.update { it.copy(playerNameError = result.reason) }
        } else {
            viewModelScope.launch {
                navigate(destination = Destination.Table(player = playerName))
            }
        }
    }
}

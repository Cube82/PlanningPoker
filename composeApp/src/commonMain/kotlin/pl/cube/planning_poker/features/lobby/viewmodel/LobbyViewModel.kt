package pl.cube.planning_poker.features.lobby.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.cube.planning_poker.helpers.PlayerNameValidator
import pl.cube.planning_poker.helpers.ValidateStatus
import pl.cube.planning_poker.models.server.KnownTables
import pl.cube.planning_poker.navi.Destination
import pl.cube.planning_poker.navi.Navigator

internal class LobbyViewModel(
    private val playerNameValidator: PlayerNameValidator,
    navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), Navigator by navigator {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()
    private val thisRoute = savedStateHandle.toRoute<Destination.Lobby>()

    var playerName by mutableStateOf(thisRoute.player.orEmpty())
        private set

    init {
        val initialTableId = thisRoute.tableId?.takeIf(::tableExists)
            ?: DEFAULT_TABLES.singleOrNull()
        val initialPlayerNameError = if (playerName.isBlank()) {
            null
        } else {
            validatePlayerNameOrNull(playerName)
        }
        _uiState.value = LobbyUiState(
            playerNameError = initialPlayerNameError,
            selectedTableId = initialTableId,
            availableTables = DEFAULT_TABLES,
        )
        navigateIfReady()
    }

    fun updatePlayerName(input: String) {
        _uiState.update { it.copy(playerNameError = null) }
        playerName = input
        navigateIfReady()
    }

    fun selectTable(tableId: String) {
        _uiState.update { it.copy(selectedTableId = tableId) }
        navigateIfReady()
    }

    fun joinTable() {
        playerName = playerName.trim()
        val playerNameError = validatePlayerNameOrNull(playerName)
        if (playerNameError != null) {
            _uiState.update { it.copy(playerNameError = playerNameError) }
            return
        }
        navigateIfReady()
    }

    private fun navigateIfReady() {
        val trimmedPlayerName = playerName.trim()
        val tableId = _uiState.value.selectedTableId
        if (trimmedPlayerName.isBlank() || tableId.isNullOrBlank()) {
            return
        }

        if (validatePlayerNameOrNull(trimmedPlayerName) != null) {
            return
        }

        viewModelScope.launch {
            navigate(
                destination = Destination.Table(
                    tableId = tableId,
                    player = trimmedPlayerName,
                ),
                options = {
                    popUpTo(thisRoute) {
                        inclusive = true
                    }
                },
            )
        }
    }

    private companion object {
        val DEFAULT_TABLES = KnownTables.publicTableIds.toList()
    }

    private fun validatePlayerNameOrNull(name: String): pl.cube.planning_poker.helpers.PlayerNameValidationError? {
        return when (val result = playerNameValidator.isPlayerNameValid(name.trim())) {
            is ValidateStatus.Invalid -> result.reason
            else -> null
        }
    }

    private fun tableExists(tableId: String): Boolean = tableId in KnownTables.publicTableIds
}

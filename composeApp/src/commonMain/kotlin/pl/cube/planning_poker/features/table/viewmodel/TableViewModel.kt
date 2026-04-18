package pl.cube.planning_poker.features.table.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.cube.planning_poker.data.GameClientImpl
import pl.cube.planning_poker.helpers.PlayerNameValidator
import pl.cube.planning_poker.helpers.ValidateStatus
import pl.cube.planning_poker.logger.Logger
import pl.cube.planning_poker.models.client.JoinTable
import pl.cube.planning_poker.models.server.GameState
import pl.cube.planning_poker.models.server.ServerError
import pl.cube.planning_poker.navi.Destination
import pl.cube.planning_poker.navi.Navigator

internal class TableViewModel(
    private val playerNameValidator: PlayerNameValidator,
    private val client: GameClientImpl,
    navigator: Navigator,
    savedState: SavedStateHandle,
) : ViewModel(), Navigator by navigator {

    private val _uiState = MutableStateFlow(TableUiState())
    val uiState: StateFlow<TableUiState> = _uiState.asStateFlow()
    private val serverMessages = client
        .getTableMessageStream()
        .onStart {
            Logger.d("connecting")
            _uiState.update { it.copy(connectionState = ConnectionState.Connecting) }
        }
        .onCompletion { cause ->
            if (cause == null) {
                Logger.d("disconnected")
                _uiState.update { state ->
                    state.copy(
                        connectionState = when (state.connectionState) {
                            ConnectionState.Error -> ConnectionState.Error
                            else -> ConnectionState.Disconnected
                        }
                    )
                }
            }
        }
        .catch { t ->
            if (t is Exception) Logger.e("error", t)
            _uiState.update { it.copy(connectionState = ConnectionState.Error) }
        }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 0)

    val gameState = serverMessages
        .filterIsInstance<GameState>()
        .onEach {
            Logger.d("connected")
            _uiState.update { it.copy(connectionState = ConnectionState.Joined) }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, GameState())
    private val thisRoute = savedState.toRoute<Destination.Table>()
    private val playerName = thisRoute.player

    init {
        _uiState.value = _uiState.value.copy(tmp = playerName)

        serverMessages
            .filterIsInstance<ServerError>()
            .onEach(::handleServerError)
            .launchIn(viewModelScope)

        if (isPlayerNameValid()) {
            joinTable()
        }
    }

    private fun isPlayerNameValid(): Boolean {
        val result = playerNameValidator.isPlayerNameValid(playerName)
        return if (result is ValidateStatus.Invalid) {
            _uiState.update {
                _uiState.value.copy(
                    alert = AlertUiState(
                        kind = TableAlertKind.InvalidPlayerName(result.reason),
                        onConfirm = ::goToLobby,
                    )
                )
            }
            false
        } else {
            true
        }
    }

    fun joinTable() {
        viewModelScope.launch {
            Logger.d("joining table")
            client.sendMessage(JoinTable(playerName, "some id"))
        }
    }

    fun leaveTable() {
        viewModelScope.launch {
            Logger.d("leaving table")
            client.close()
        }
    }

    private fun handleServerError(error: ServerError) {
        _uiState.update {
            it.copy(
                alert = AlertUiState(
                    kind = TableAlertKind.ServerProblem(
                        code = error.code,
                        fallbackMessage = error.message,
                    ),
                    onConfirm = ::goToLobby,
                )
            )
        }
    }

    private fun goToLobby() {
        _uiState.value = _uiState.value.copy(alert = null)
        viewModelScope.launch {
            navigate(
                destination = Destination.Lobby,
                options = {
                    popUpTo(thisRoute) {
                        inclusive = true
                    }
                },
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        leaveTable()
    }
}

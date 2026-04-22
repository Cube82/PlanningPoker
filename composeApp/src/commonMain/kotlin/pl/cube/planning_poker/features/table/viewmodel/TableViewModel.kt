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
import pl.cube.planning_poker.models.client.ResetRound
import pl.cube.planning_poker.models.client.RevealCards
import pl.cube.planning_poker.models.client.SelectCard
import pl.cube.planning_poker.models.server.PlanningCard
import pl.cube.planning_poker.models.server.ServerError
import pl.cube.planning_poker.models.server.TableState
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

    val tableState = serverMessages
        .filterIsInstance<TableState>()
        .onEach {
            Logger.d("connected")
            _uiState.update { it.copy(connectionState = ConnectionState.Joined) }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, TableState())
    private val thisRoute = savedState.toRoute<Destination.Table>()
    private val playerName = thisRoute.player.orEmpty()
    private val tableId = thisRoute.tableId

    init {
        _uiState.value = _uiState.value.copy(tmp = playerName)

        if (playerName.isBlank()) {
            goToLobby()
        } else {
            serverMessages
                .filterIsInstance<ServerError>()
                .onEach(::handleServerError)
                .launchIn(viewModelScope)

            if (isPlayerNameValid()) {
                joinTable()
            }
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
        runClientAction("joining table") {
            client.sendMessage(JoinTable(playerName, tableId))
        }
    }

    fun selectCard(card: PlanningCard) {
        runClientAction("selecting card $card") {
            client.sendMessage(SelectCard(card))
        }
    }

    fun revealCards() {
        runClientAction("revealing cards") {
            client.sendMessage(RevealCards)
        }
    }

    fun resetRound() {
        runClientAction("resetting round") {
            client.sendMessage(ResetRound)
        }
    }

    fun leaveTable() {
        viewModelScope.launch {
            Logger.d("leaving table")
            try {
                client.close()
            } catch (e: Exception) {
                Logger.e("error closing table", e)
            }
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

    private fun handleConnectionProblem(exception: Exception) {
        Logger.e("connection problem", exception)
        _uiState.update {
            it.copy(
                connectionState = ConnectionState.Error,
                alert = AlertUiState(
                    kind = TableAlertKind.ConnectionProblem(
                        fallbackMessage = exception.message ?: "",
                    ),
                    onConfirm = ::goToLobby,
                ),
            )
        }
    }

    private fun runClientAction(
        logMessage: String,
        action: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            Logger.d(logMessage)
            try {
                action()
            } catch (e: Exception) {
                handleConnectionProblem(e)
            }
        }
    }

    private fun goToLobby() {
        _uiState.value = _uiState.value.copy(alert = null)
        viewModelScope.launch {
            navigate(
                destination = Destination.Lobby(tableId = tableId),
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

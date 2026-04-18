package pl.cube.planning_poker.features.table.viewmodel

import androidx.compose.runtime.Stable
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.models.server.ServerErrorCode

@Stable
internal data class TableUiState(
    val tmp: String = "",
    val connectionState: ConnectionState = ConnectionState.Idle,
    val alert: AlertUiState? = null
)

internal enum class ConnectionState {
    Idle,
    Connecting,
    Joined,
    Disconnected,
    Error,
}

@Stable
internal data class AlertUiState(
    val kind: TableAlertKind,
    val onConfirm: () -> Unit,
)

internal sealed interface TableAlertKind {
    data class InvalidPlayerName(val reason: PlayerNameValidationError) : TableAlertKind
    data class ServerProblem(val code: ServerErrorCode, val fallbackMessage: String) : TableAlertKind
}

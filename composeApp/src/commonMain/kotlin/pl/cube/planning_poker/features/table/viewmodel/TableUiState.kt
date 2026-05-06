package pl.cube.planning_poker.features.table.viewmodel

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.models.server.ServerErrorCode
import planningpoker.composeapp.generated.resources.*

@Stable
internal data class TableUiState(
    val playerName: String = "",
    val tableName: String = "",
    val connectionState: ConnectionState = ConnectionState.Idle,
    val alert: AlertUiState? = null
)

internal enum class ConnectionState(
    val labelRes: StringResource,
) {
    Idle(Res.string.connection_state_idle),
    Connecting(Res.string.connection_state_connecting),
    Joined(Res.string.connection_state_joined),
    Disconnected(Res.string.connection_state_disconnected),
    Error(Res.string.connection_state_error),
}

@Stable
internal data class AlertUiState(
    val kind: TableAlertKind,
    val onConfirm: () -> Unit,
)

internal sealed interface TableAlertKind {
    data class InvalidPlayerName(val reason: PlayerNameValidationError) : TableAlertKind
    data class ServerProblem(val code: ServerErrorCode, val fallbackMessage: String) :
        TableAlertKind

    data class ConnectionProblem(val fallbackMessage: String) : TableAlertKind
}

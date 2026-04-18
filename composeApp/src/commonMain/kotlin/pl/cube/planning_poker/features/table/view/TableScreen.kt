package pl.cube.planning_poker.features.table.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pl.cube.planning_poker.features.table.viewmodel.AlertUiState
import pl.cube.planning_poker.features.table.viewmodel.ConnectionState
import pl.cube.planning_poker.features.table.viewmodel.TableUiState
import pl.cube.planning_poker.features.table.viewmodel.TableAlertKind
import pl.cube.planning_poker.features.table.viewmodel.TableViewModel
import pl.cube.planning_poker.helpers.fillWidthWide
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.models.server.Player
import pl.cube.planning_poker.models.server.ServerErrorCode
import pl.cube.planning_poker.ui.SimpleAlertDialog
import pl.cube.planning_poker.ui.dimen16
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.connection_state_connecting
import planningpoker.composeapp.generated.resources.connection_state_disconnected
import planningpoker.composeapp.generated.resources.connection_state_error
import planningpoker.composeapp.generated.resources.connection_state_idle
import planningpoker.composeapp.generated.resources.connection_state_joined
import planningpoker.composeapp.generated.resources.lobby_player_name_error_invalid_length
import planningpoker.composeapp.generated.resources.table_alert_invalid_player_button
import planningpoker.composeapp.generated.resources.table_alert_invalid_player_title
import planningpoker.composeapp.generated.resources.table_alert_server_user_name_taken
import planningpoker.composeapp.generated.resources.table_join_button
import planningpoker.composeapp.generated.resources.table_title

@Composable
internal fun TableScreen(viewModel: TableViewModel = koinViewModel()) {
    DisposableEffect(viewModel) {
        onDispose {
            viewModel.leaveTable()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    TableScreenLayout(
        state = state,
        players = gameState.players,
        joinTable = viewModel::joinTable,
    )
}

@Composable
private fun TableScreenLayout(
    state: TableUiState,
    players: List<Player>,
    joinTable: () -> Unit,
) {
    if (state.alert != null) {
        val alertText = state.alert.toText()
        SimpleAlertDialog(
            message = alertText.message,
            title = alertText.title,
            button = alertText.button,
            onConfirm = state.alert.onConfirm,
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card {
            Column(
                modifier = Modifier.padding(dimen16).fillWidthWide(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.table_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(state.tmp)
                Text(
                    text = when (state.connectionState) {
                        ConnectionState.Idle -> stringResource(Res.string.connection_state_idle)
                        ConnectionState.Connecting -> stringResource(Res.string.connection_state_connecting)
                        ConnectionState.Joined -> stringResource(Res.string.connection_state_joined)
                        ConnectionState.Disconnected -> stringResource(Res.string.connection_state_disconnected)
                        ConnectionState.Error -> stringResource(Res.string.connection_state_error)
                    }
                )
                Button(joinTable) {
                    Text(stringResource(Res.string.table_join_button))
                }
                players.fastForEach { player ->
                    Text(player.toString())
                }
            }
        }
    }
}

@Composable
private fun AlertUiState.toText(): AlertText = when (kind) {
    is TableAlertKind.InvalidPlayerName -> AlertText(
        title = stringResource(Res.string.table_alert_invalid_player_title),
        message = when (kind.reason) {
            PlayerNameValidationError.InvalidLength ->
                stringResource(Res.string.lobby_player_name_error_invalid_length)
        },
        button = stringResource(Res.string.table_alert_invalid_player_button),
    )

    is TableAlertKind.ServerProblem -> AlertText(
        title = stringResource(Res.string.table_alert_invalid_player_title),
        message = when (kind.code) {
            ServerErrorCode.UserNameTaken -> stringResource(Res.string.table_alert_server_user_name_taken)
            else -> kind.fallbackMessage
        },
        button = stringResource(Res.string.table_alert_invalid_player_button),
    )
}

private data class AlertText(
    val title: String,
    val message: String,
    val button: String,
)

package pl.cube.planning_poker.features.table.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import pl.cube.planning_poker.features.table.viewmodel.TableAlertKind
import pl.cube.planning_poker.features.table.viewmodel.TableUiState
import pl.cube.planning_poker.features.table.viewmodel.TableViewModel
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.helpers.fillWidthWide
import pl.cube.planning_poker.models.server.PlanningCard
import pl.cube.planning_poker.models.server.PlayerRole
import pl.cube.planning_poker.models.server.PlayerState
import pl.cube.planning_poker.models.server.PublicVoteState
import pl.cube.planning_poker.models.server.RoundStatus
import pl.cube.planning_poker.models.server.ServerErrorCode
import pl.cube.planning_poker.models.server.TableActionPermission
import pl.cube.planning_poker.models.server.TableState
import pl.cube.planning_poker.models.server.displayLabel
import pl.cube.planning_poker.ui.SimpleAlertDialog
import pl.cube.planning_poker.ui.dimen4
import pl.cube.planning_poker.ui.dimen16
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.connection_state_connecting
import planningpoker.composeapp.generated.resources.connection_state_disconnected
import planningpoker.composeapp.generated.resources.connection_state_error
import planningpoker.composeapp.generated.resources.connection_state_idle
import planningpoker.composeapp.generated.resources.connection_state_joined
import planningpoker.composeapp.generated.resources.lobby_player_name_error_invalid_length
import planningpoker.composeapp.generated.resources.table_alert_invalid_player_button
import planningpoker.composeapp.generated.resources.table_alert_connection_problem
import planningpoker.composeapp.generated.resources.table_alert_invalid_player_title
import planningpoker.composeapp.generated.resources.table_alert_server_table_not_found
import planningpoker.composeapp.generated.resources.table_alert_server_user_name_taken
import planningpoker.composeapp.generated.resources.table_host_suffix
import planningpoker.composeapp.generated.resources.table_my_vote_label
import planningpoker.composeapp.generated.resources.table_no_vote
import planningpoker.composeapp.generated.resources.table_player_row
import planningpoker.composeapp.generated.resources.table_player_status_not_voted
import planningpoker.composeapp.generated.resources.table_player_status_voted
import planningpoker.composeapp.generated.resources.table_reset_button
import planningpoker.composeapp.generated.resources.table_reveal_button
import planningpoker.composeapp.generated.resources.table_round_label
import planningpoker.composeapp.generated.resources.table_title

@Composable
internal fun TableScreen(viewModel: TableViewModel = koinViewModel()) {
    DisposableEffect(viewModel) {
        onDispose {
            viewModel.leaveTable()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val tableState by viewModel.tableState.collectAsStateWithLifecycle()
    TableScreenLayout(
        state = state,
        tableState = tableState,
        selectCard = viewModel::selectCard,
        revealCards = viewModel::revealCards,
        resetRound = viewModel::resetRound,
    )
}

@Composable
private fun TableScreenLayout(
    state: TableUiState,
    tableState: TableState,
    selectCard: (PlanningCard) -> Unit,
    revealCards: () -> Unit,
    resetRound: () -> Unit,
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
                Text(stringResource(Res.string.table_round_label, tableState.round.number))
                Text(
                    stringResource(
                        Res.string.table_my_vote_label,
                        tableState.round.selfVote?.displayLabel()
                            ?: stringResource(Res.string.table_no_vote),
                    )
                )
                RoundActions(
                    tableState = tableState,
                    enabled = state.connectionState == ConnectionState.Joined,
                    onReveal = revealCards,
                    onReset = resetRound,
                )
                PlayerList(players = tableState.players)
                CardPicker(
                    deck = tableState.deck,
                    selectedCard = tableState.round.selfVote,
                    enabled = state.connectionState == ConnectionState.Joined && tableState.round.status == RoundStatus.Voting,
                    onSelect = selectCard,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun RoundActions(
    tableState: TableState,
    enabled: Boolean,
    onReveal: () -> Unit,
    onReset: () -> Unit,
) {
    val canReveal = tableState.canExecute(tableState.revealPermission)
    val canReset = tableState.canExecute(tableState.resetPermission)

    if (!canReveal && !canReset) {
        return
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        if (canReveal) {
            Button(
                onClick = onReveal,
                enabled = enabled && tableState.round.status == RoundStatus.Voting,
                modifier = Modifier.padding(dimen4),
            ) {
                Text(stringResource(Res.string.table_reveal_button))
            }
        }

        if (canReset) {
            Button(
                onClick = onReset,
                enabled = enabled,
                modifier = Modifier.padding(dimen4),
            ) {
                Text(stringResource(Res.string.table_reset_button))
            }
        }
    }
}

@Composable
private fun PlayerList(players: List<PlayerState>) {
    players.fastForEach { player ->
        val hostSuffix = if (player.role == PlayerRole.Host) {
            stringResource(Res.string.table_host_suffix)
        } else {
            ""
        }
        Text(
            stringResource(
                Res.string.table_player_row,
                player.name,
                hostSuffix,
                player.vote.label(),
            )
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CardPicker(
    deck: List<PlanningCard>,
    selectedCard: PlanningCard?,
    enabled: Boolean,
    onSelect: (PlanningCard) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        deck.fastForEach { card ->
            Button(
                onClick = { onSelect(card) },
                enabled = enabled,
                modifier = Modifier.padding(dimen4),
            ) {
                Text(
                    if (selectedCard == card) {
                        "[${card.displayLabel()}]"
                    } else {
                        card.displayLabel()
                    }
                )
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
            ServerErrorCode.TableNotFound -> stringResource(Res.string.table_alert_server_table_not_found)
            else -> kind.fallbackMessage
        },
        button = stringResource(Res.string.table_alert_invalid_player_button),
    )

    is TableAlertKind.ConnectionProblem -> AlertText(
        title = stringResource(Res.string.table_alert_invalid_player_title),
        message = kind.fallbackMessage.ifBlank {
            stringResource(Res.string.table_alert_connection_problem)
        },
        button = stringResource(Res.string.table_alert_invalid_player_button),
    )
}

private data class AlertText(
    val title: String,
    val message: String,
    val button: String,
)

@Composable
private fun PublicVoteState.label(): String = when (this) {
    PublicVoteState.NotVoted -> stringResource(Res.string.table_player_status_not_voted)
    PublicVoteState.VotedHidden -> stringResource(Res.string.table_player_status_voted)
    is PublicVoteState.Revealed -> card.displayLabel()
}

private fun TableState.canExecute(permission: TableActionPermission): Boolean = when (permission) {
    TableActionPermission.HostOnly -> selfPlayerId == hostId
    TableActionPermission.Anyone -> selfPlayerId != null
}

package pl.cube.planning_poker.features.table.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pl.cube.planning_poker.features.table.viewmodel.*
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.helpers.fillWidthWide
import pl.cube.planning_poker.models.server.*
import pl.cube.planning_poker.ui.*
import pl.cube.planning_poker.ui.components.*
import pl.cube.planning_poker.ui.settings.AppSettingsState
import pl.cube.planning_poker.ui.settings.AppThemeMode
import planningpoker.composeapp.generated.resources.*

@Composable
internal fun TableScreen(
    settingsState: AppSettingsState,
    onThemeModeChange: (AppThemeMode) -> Unit,
    viewModel: TableViewModel = koinViewModel(),
) {
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
        settingsState = settingsState,
        selectCard = viewModel::selectCard,
        unselectCard = viewModel::unselectCard,
        revealCards = viewModel::revealCards,
        resetRound = viewModel::resetRound,
        onThemeModeChange = onThemeModeChange,
    )
}

@Composable
private fun TableScreenLayout(
    state: TableUiState,
    tableState: TableState,
    settingsState: AppSettingsState,
    selectCard: (PlanningCard) -> Unit,
    unselectCard: () -> Unit,
    revealCards: () -> Unit,
    resetRound: () -> Unit,
    onThemeModeChange: (AppThemeMode) -> Unit,
) {
    if (state.alert != null) {
        val alertText = state.alert.toText()
        AppAlertDialog(
            message = alertText.message,
            title = alertText.title,
            button = alertText.button,
            onConfirm = state.alert.onConfirm,
        )
    }

    Scaffold(
        topBar = {
            TableTopBar(
                tableName = state.tableName,
                connectionState = state.connectionState,
                settingsState = settingsState,
                onThemeModeChange = onThemeModeChange,
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier.fillWidthWide(maxWidth = dimenExtraWide),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RoundActions(
                    tableState = tableState,
                    enabled = state.connectionState == ConnectionState.Joined,
                    onReveal = revealCards,
                    onReset = resetRound,
                )

                AppHeadline(
                    text = pluralStringResource(
                        resource = Res.plurals.table_headline_participants_count,
                        quantity = tableState.players.count(),
                        tableState.players.count(),
                    ),
                )
                Column(
                    modifier = Modifier.fillWidthWide(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PlayersList(
                        players = tableState.players,
                        selfPlayerId = tableState.selfPlayerId,
                    )
                }

                Column(
                    modifier = Modifier.fillWidthWide(dimenExtraWide),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AppHeadline(stringResource(Res.string.table_headline_estimate))

                    CardPicker(
                        deck = tableState.deck,
                        selectedCard = tableState.round.selfVote,
                        enabled = state.connectionState == ConnectionState.Joined && tableState.round.status == RoundStatus.Voting,
                        onSelect = selectCard,
                        onUnselect = unselectCard,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TableTopBar(
    tableName: String,
    connectionState: ConnectionState,
    settingsState: AppSettingsState,
    onThemeModeChange: (AppThemeMode) -> Unit,
) {
    val connectionStateText = stringResource(connectionState.labelRes)

    TopAppBar(
        title = {
            Column {
                AppLogo()
                AppSpacer(dimen4)
                AppText(
                    text = if (tableName.isBlank()) {
                        stringResource(Res.string.table_title)
                    } else {
                        "${stringResource(Res.string.table_title)} - $tableName ($connectionStateText)"
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        actions = {
            AppSettingsMenu(
                state = settingsState,
                onThemeModeChange = onThemeModeChange,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
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
            AppButton(
                text = Res.string.table_reveal_button,
                onClick = onReveal,
                modifier = Modifier.padding(dimen4),
                enabled = enabled && tableState.round.status == RoundStatus.Voting,
                icon = Res.drawable.visibility_24px,
            )
        }

        if (canReset) {
            AppButton(
                text = Res.string.table_reset_button,
                onClick = onReset,
                modifier = Modifier.padding(dimen4),
                enabled = enabled,
                icon = Res.drawable.refresh_24px,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CardPicker(
    deck: List<PlanningCard>,
    selectedCard: PlanningCard?,
    enabled: Boolean,
    onSelect: (PlanningCard) -> Unit,
    onUnselect: () -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimen8, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(dimen8),
    ) {
        deck.fastForEach { card ->
            BigCard(
                card = card,
                selected = selectedCard == card,
                enabled = enabled,
                onClick = {
                    if (selectedCard == card) {
                        onUnselect()
                    } else {
                        onSelect(card)
                    }
                },
            )
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

private fun TableState.canExecute(permission: TableActionPermission): Boolean = when (permission) {
    TableActionPermission.HostOnly -> selfPlayerId == hostId
    TableActionPermission.Anyone -> selfPlayerId != null
}

@MultiPreview
@Composable
fun TableScreenVotingPreview() {
    TableScreenPreviewContent(tableState = TablePreviewFixtures.tableStateVoting)
}

@MultiPreview
@Composable
fun TableScreenRevealedPreview() {
    TableScreenPreviewContent(tableState = TablePreviewFixtures.tableStateRevealed)
}

@Composable
private fun TableScreenPreviewContent(tableState: TableState) {
    DefaultPreview {
        TableScreenLayout(
            state = TablePreviewFixtures.uiState,
            tableState = tableState,
            settingsState = AppSettingsState(),
            selectCard = {},
            unselectCard = {},
            revealCards = {},
            resetRound = {},
            onThemeModeChange = {},
        )
    }
}

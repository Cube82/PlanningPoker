package pl.cube.planning_poker.features.lobby.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pl.cube.planning_poker.features.lobby.viewmodel.LobbyViewModel
import pl.cube.planning_poker.helpers.PlayerNameValidationError
import pl.cube.planning_poker.helpers.fillWidthWide
import pl.cube.planning_poker.ui.*
import pl.cube.planning_poker.ui.components.*
import planningpoker.composeapp.generated.resources.*

@Composable
internal fun LobbyScreen(
    viewModel: LobbyViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LobbyLayout(
        playerName = viewModel.playerName,
        lastPlayerName = state.lastPlayerName,
        playerNameError = state.playerNameError,
        selectedTableId = state.selectedTableId,
        availableTables = state.availableTables,
        updatePlayerName = viewModel::updatePlayerName,
        selectTable = viewModel::selectTable,
        onJoinTableClick = viewModel::joinTable,
    )
}

@Composable
internal fun LobbyLayout(
    playerName: String,
    lastPlayerName: String,
    playerNameError: PlayerNameValidationError?,
    selectedTableId: String?,
    availableTables: List<String>,
    updatePlayerName: (String) -> Unit,
    selectTable: (String) -> Unit,
    onJoinTableClick: () -> Unit,
) {
    val needsPlayerName = playerName.isBlank() || playerNameError != null
    val needsTable = selectedTableId.isNullOrBlank()
    var playerNameInput by remember(playerName) {
        mutableStateOf(playerName.ifBlank { lastPlayerName })
    }

    fun submitPlayerName() {
        updatePlayerName(playerNameInput)
        onJoinTableClick()
    }

    AppScaffold(
        subtitle = stringResource(Res.string.lobby_title),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppSpacer(dimen16)
            AppCard {
                Column(
                    modifier = Modifier.padding(dimen32).fillWidthWide(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AppText(
                        text = Res.string.lobby_welcome,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AppSpacer(dimen16)
                    AppText(
                        text = stringResource(Res.string.lobby_player_before_info),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AppSpacer(dimen16)
                    AppTextField(
                        value = playerNameInput,
                        onValueChange = { playerNameInput = it },
                        placeholder = stringResource(Res.string.lobby_player_name_label),
                        modifier = Modifier.fillMaxWidth().onKeyEvent { event ->
                            if (event.key.keyCode == Key.Enter.keyCode) {
                                submitPlayerName()
                                true
                            } else {
                                false
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { submitPlayerName() }),
                        isError = playerNameError != null,
                        supportingText = {
                            playerNameError?.let { error ->
                                AppText(error.toMessage())
                            }
                        },
                    )

                    AppSpacer(dimen16)

                    if (needsTable) {
                        AppText(
                            text = stringResource(Res.string.lobby_table_label),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        AppSpacer(dimen8)
                        availableTables.forEach { tableId ->
                            AppButton(
                                text = stringResource(Res.string.lobby_table_option, tableId),
                                onClick = { selectTable(tableId) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    } else {
                        AppText(
                            text = stringResource(Res.string.lobby_selected_table, selectedTableId),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    AppSpacer(dimen16)

                    if (needsPlayerName || !needsTable) {
                        AppDivider()
                        AppSpacer(dimen16)
                        AppButton(
                            text = Res.string.lobby_join_button,
                            onClick = ::submitPlayerName,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            AppSpacer(dimen32)
            AppLogoBig()
        }
    }
}

@Composable
private fun PlayerNameValidationError.toMessage(): String = when (this) {
    PlayerNameValidationError.InvalidLength -> stringResource(Res.string.lobby_player_name_error_invalid_length)
}

@MultiPreview
@Composable
private fun LobbyScreenPreview() {
    DefaultPreview {
        LobbyLayout(
            playerName = "",
            lastPlayerName = "",
            playerNameError = null,
            selectedTableId = null,
            availableTables = listOf("main"),
            updatePlayerName = {},
            selectTable = {},
            onJoinTableClick = {},
        )
    }
}

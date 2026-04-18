package pl.cube.planning_poker.features.lobby.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import pl.cube.planning_poker.ui.dimen16
import planningpoker.composeapp.generated.resources.*

@Composable
internal fun LobbyScreen(viewModel: LobbyViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LobbyLayout(
        playerName = viewModel.playerName,
        playerNameError = state.playerNameError,
        updatePlayerName = viewModel::updatePlayerName,
        onJoinTableClick = viewModel::joinTable,
    )
}

@Composable
internal fun LobbyLayout(
    playerName: String,
    playerNameError: PlayerNameValidationError?,
    updatePlayerName: (String) -> Unit,
    onJoinTableClick: () -> Unit,
) {
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
                    text = stringResource(Res.string.lobby_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(Modifier.size(dimen16))

                OutlinedTextField(
                    value = playerName,
                    onValueChange = updatePlayerName,
                    modifier = Modifier.fillMaxWidth().onKeyEvent { event ->
                            if (event.key.keyCode == Key.Enter.keyCode) {
                                onJoinTableClick()
                                true
                            } else {
                                false
                            }
                        },
                    label = { Text(stringResource(Res.string.lobby_player_name_label)) },
                    supportingText = {
                        playerNameError?.let { error ->
                            Text(error.toMessage())
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onJoinTableClick() }),
                    isError = playerNameError != null,
                    singleLine = true,
                )

                Button(onClick = onJoinTableClick) {
                    Text(stringResource(Res.string.lobby_join_button))
                }
            }
        }
    }
}

@Composable
private fun PlayerNameValidationError.toMessage(): String = when (this) {
    PlayerNameValidationError.InvalidLength -> stringResource(Res.string.lobby_player_name_error_invalid_length)
}

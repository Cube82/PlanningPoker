package pl.cube.planning_poker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.cube.planning_poker.helpers.noop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimpleAlertDialog(
    message: String,
    title: String? = null,
    button: String = "ok",
    onDismissRequest: () -> Unit = noop,
    onConfirm: () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.wrapContentSize().padding(dimen16),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(dimen16).defaultMinSize(minWidth = 240.dp)) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.height(dimen4))
                }
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(dimen16))
                TextButton(
                    onClick = { onConfirm.invoke() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(button)
                }
            }
        }
    }
}
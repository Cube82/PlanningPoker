package pl.cube.planning_poker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.cube.planning_poker.helpers.noop
import pl.cube.planning_poker.ui.components.AppSpacer
import pl.cube.planning_poker.ui.components.AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppAlertDialog(
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
                    AppText(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    AppSpacer(dimen4)
                }
                AppText(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                )
                AppSpacer(dimen16)
                TextButton(
                    onClick = { onConfirm.invoke() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    AppText(button)
                }
            }
        }
    }
}
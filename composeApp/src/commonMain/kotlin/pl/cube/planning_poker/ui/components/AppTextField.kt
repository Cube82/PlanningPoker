package pl.cube.planning_poker.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import pl.cube.planning_poker.ui.dimen05
import pl.cube.planning_poker.ui.dimen1
import pl.cube.planning_poker.ui.theme.AppThemeExtras

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: (@Composable (() -> Unit))? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val appColors = AppThemeExtras.colors
    val textStyle = MaterialTheme.typography.bodyLarge
    val cursorColor = MaterialTheme.colorScheme.onSurface
    val colors = OutlinedTextFieldDefaults.colors().copy(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = appColors.textFieldContainer,
        unfocusedContainerColor = appColors.textFieldContainer,
        focusedIndicatorColor = appColors.textFieldBorder,
        unfocusedIndicatorColor = appColors.textFieldBorder,
        focusedPlaceholderColor = appColors.textFieldPlaceholder,
        unfocusedPlaceholderColor = appColors.textFieldPlaceholder,
        cursorColor = cursorColor,
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(cursorColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                isError = isError,
                placeholder = {
                    AppText(
                        text = placeholder,
                        style = textStyle,
                    )
                },
                supportingText = supportingText,
                colors = colors,
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = ShapeDefaults.Medium,
                        focusedBorderThickness = dimen1,
                        unfocusedBorderThickness = dimen05,
                    )
                },
            )
        },
    )
}

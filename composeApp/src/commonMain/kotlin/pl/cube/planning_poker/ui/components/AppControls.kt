package pl.cube.planning_poker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.cube.planning_poker.ui.dimen1
import pl.cube.planning_poker.ui.dimen16
import pl.cube.planning_poker.ui.dimen8
import pl.cube.planning_poker.ui.theme.AppThemeExtras

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = ShapeDefaults.Medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimen8,
        ),
        content = content,
    )
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: DrawableResource? = null,
    iconContentDescription: String? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ShapeDefaults.Medium,
        colors = ButtonDefaults.buttonColors(),
        content = {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = iconContentDescription,
                )
                AppSpacer(dimen8)
            }
            AppText(text)
        },
    )
}

@Composable
fun AppButton(
    text: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: DrawableResource? = null,
    iconContentDescription: String? = null,
) {
    AppButton(
        text = stringResource(text),
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        iconContentDescription = iconContentDescription,
    )
}

@Composable
fun AppDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = dimen1,
        color = AppThemeExtras.colors.divider,
    )
}

@Composable
fun AppHeadline(
    text: String,
) {
    Column {
        AppSpacer(dimen16)
        AppText(
            text = text.uppercase(),
            style = MaterialTheme.typography.bodySmall,
        )
        AppSpacer(dimen8)
        AppDivider()
        AppSpacer(dimen16)
    }
}

@Composable
fun AppSpacer(size: Dp) {
    Spacer(Modifier.size(size))
}

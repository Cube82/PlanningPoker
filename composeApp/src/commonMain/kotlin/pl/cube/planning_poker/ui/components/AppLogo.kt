package pl.cube.planning_poker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pl.cube.planning_poker.ui.DefaultPreview
import pl.cube.planning_poker.ui.dimen8
import pl.cube.planning_poker.ui.theme.AppThemeExtras
import pl.cube.planning_poker.ui.theme.LocalIsDarkTheme
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.app_logo_dark
import planningpoker.composeapp.generated.resources.app_logo_light

@Composable
fun AppLogo() {
    Row {
        LogoTexts()
    }
}

@Composable
fun AppLogoBig() {
    val isDark = LocalIsDarkTheme.current
    val logoResource = if (isDark) Res.drawable.app_logo_dark else Res.drawable.app_logo_light

    Row(
        modifier = Modifier.alpha(0.25f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(logoResource),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(Modifier.width(dimen8))
        Column {
            LogoTexts()
        }
    }
}

@Composable
private fun LogoTexts() {
    val fontSize = 24.sp
    val lineHeight = 30.sp
    val letterSpacing = (-0.2).sp

    AppText(
        text = "planning",
        style = TextStyle(
            color = AppThemeExtras.colors.logo,
            fontWeight = FontWeight.Light,
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
        ),
    )
    AppText(
        text = "poker",
        style = TextStyle(
            color = AppThemeExtras.colors.logo,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
        )
    )
}

@PreviewLightDark
@Composable
fun AppLogoPreview() {
    DefaultPreview {
        AppLogo()
    }
}

@PreviewLightDark
@Composable
fun AppLogoBigPreview() {
    DefaultPreview {
        AppLogoBig()
    }
}

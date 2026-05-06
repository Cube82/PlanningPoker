package pl.cube.planning_poker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import planningpoker.composeapp.generated.resources.Res
import planningpoker.composeapp.generated.resources.inter_variable as InterVariable

@Composable
fun appTypography(): Typography {
    val interFontFamily = FontFamily(
        Font(Res.font.InterVariable, weight = FontWeight.Normal),
        Font(Res.font.InterVariable, weight = FontWeight.Medium),
        Font(Res.font.InterVariable, weight = FontWeight.SemiBold),
        Font(Res.font.InterVariable, weight = FontWeight.Bold),
        Font(Res.font.InterVariable, weight = FontWeight.Light),
    )

    return Typography(
        titleLarge = TextStyle(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = (-0.2).sp,
        ),
        titleMedium = TextStyle(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    )
}

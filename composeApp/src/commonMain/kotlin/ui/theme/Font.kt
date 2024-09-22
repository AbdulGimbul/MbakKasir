package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import mbakkasir.composeapp.generated.resources.Poppins_Black
import mbakkasir.composeapp.generated.resources.Poppins_BlackItalic
import mbakkasir.composeapp.generated.resources.Poppins_Bold
import mbakkasir.composeapp.generated.resources.Poppins_BoldItalic
import mbakkasir.composeapp.generated.resources.Poppins_ExtraBold
import mbakkasir.composeapp.generated.resources.Poppins_ExtraBoldItalic
import mbakkasir.composeapp.generated.resources.Poppins_ExtraLight
import mbakkasir.composeapp.generated.resources.Poppins_ExtraLightItalic
import mbakkasir.composeapp.generated.resources.Poppins_Italic
import mbakkasir.composeapp.generated.resources.Poppins_Light
import mbakkasir.composeapp.generated.resources.Poppins_LightItalic
import mbakkasir.composeapp.generated.resources.Poppins_Medium
import mbakkasir.composeapp.generated.resources.Poppins_MediumItalic
import mbakkasir.composeapp.generated.resources.Poppins_Regular
import mbakkasir.composeapp.generated.resources.Poppins_SemiBold
import mbakkasir.composeapp.generated.resources.Poppins_SemiBoldItalic
import mbakkasir.composeapp.generated.resources.Poppins_Thin
import mbakkasir.composeapp.generated.resources.Poppins_ThinItalic
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun PoppinsFontFamily() = FontFamily(
    Font(Res.font.Poppins_Regular, FontWeight.Normal),
    Font(Res.font.Poppins_Black, FontWeight.Black),
    Font(Res.font.Poppins_BlackItalic, FontWeight.Black, FontStyle.Italic),
    Font(Res.font.Poppins_Bold, FontWeight.Bold),
    Font(Res.font.Poppins_BoldItalic, FontWeight.Bold, FontStyle.Italic),
    Font(Res.font.Poppins_ExtraBold, FontWeight.ExtraBold),
    Font(Res.font.Poppins_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(Res.font.Poppins_ExtraLight, FontWeight.ExtraLight),
    Font(Res.font.Poppins_ExtraLightItalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(Res.font.Poppins_Italic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.Poppins_Light, FontWeight.Light),
    Font(Res.font.Poppins_LightItalic, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.Poppins_Medium, FontWeight.Medium),
    Font(Res.font.Poppins_MediumItalic, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.Poppins_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Poppins_SemiBoldItalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(Res.font.Poppins_Thin, FontWeight.Thin),
    Font(Res.font.Poppins_ThinItalic, FontWeight.Thin, FontStyle.Italic)
)

@Composable
fun PoppinsTypography() = Typography().run {

    val fontFamily = PoppinsFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}
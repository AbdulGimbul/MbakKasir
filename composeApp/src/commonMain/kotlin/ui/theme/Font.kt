package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
fun PoppinsTypography(): Typography {
    val poppinsFontFamily = FontFamily(
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

    return Typography(
        displayLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 36.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
        )
    )
}
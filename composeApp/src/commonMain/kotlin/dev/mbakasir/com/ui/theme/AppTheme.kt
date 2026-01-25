package dev.mbakasir.com.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Light color scheme for MbakKasir app */
private val LightColorScheme =
        lightColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                secondary = secondary,
                onSecondary = dark,
                secondaryContainer = secondaryContainer,
                onSecondaryContainer = dark,
                tertiary = blue,
                onTertiary = Color.White,
                tertiaryContainer = blueLight,
                onTertiaryContainer = blue,
                error = red,
                onError = Color.White,
                errorContainer = redLight,
                onErrorContainer = red,
                background = surfaceVariant,
                onBackground = dark,
                surface = surface,
                onSurface = dark,
                surfaceVariant = surfaceVariant,
                onSurfaceVariant = primaryText,
                outline = stroke,
                outlineVariant = strokeLight,
                scrim = Color.Black.copy(alpha = 0.32f),
                inverseSurface = dark,
                inverseOnSurface = Color.White,
                inversePrimary = primaryLight
        )

/** Main theme for MbakKasir app. Wraps MaterialTheme with custom colors, typography, and shapes. */
@Composable
fun MbakKasirTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    // For now, we only support light theme
    // Dark theme can be added later by creating a DarkColorScheme
    val colorScheme = LightColorScheme

    MaterialTheme(colorScheme = colorScheme, typography = PoppinsTypography(), content = content)
}

/** Extension function to get custom colors that aren't part of Material3 ColorScheme */
val ColorScheme.success: Color
    @Composable get() = green

val ColorScheme.successContainer: Color
    @Composable get() = greenLight

val ColorScheme.warning: Color
    @Composable get() = yellow

val ColorScheme.warningContainer: Color
    @Composable get() = yellowLight

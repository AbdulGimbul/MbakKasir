package dev.mbakasir.com.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val primary = Color(0xFF13C296)
val primaryLight = Color(0xFF4DDAB5)
val primaryDark = Color(0xFF0E9A78)
val primaryContainer = Color(0xFFE8FBF5)
val onPrimary = Color.White
val onPrimaryContainer = Color(0xFF0E9A78)

// Secondary Colors
val secondary = Color(0xFFEEEED6) // Fixed: was 0xFF8EEEED6 (typo)
val secondaryContainer = Color(0xFFF7F7EB)

// Text Colors
val primaryText = Color(0xFF637381)
val secondaryText = Color(0xFF8899A8)
val tertiaryText = Color(0xFFB0B8C1)

// Surface Colors
val surface = Color(0xFFFFFFFF)
val surfaceVariant = Color(0xFFF8F9FA)
val surfaceContainer = Color(0xFFF1F3F5)
val surfaceContainerHigh = Color(0xFFE9ECEF)

// Accent Colors
val stroke = Color(0xFFDFE4EA)
val strokeLight = Color(0xFFEDF0F3)
val dark = Color(0xFF111928)
val disabled = Color(0xFFE5E7EB)
val icon = Color(0xFF9CA3AF)

// Semantic Colors
val blue = Color(0xFF2D68F8)
val blueLight = Color(0xFFE8EFFF)
val red = Color(0xFFF23030)
val redLight = Color(0xFFFFE5E5)
val yellow = Color(0xFFFBBF24)
val yellowLight = Color(0xFFFFF8E1)
val green = Color(0xFF22C55E)
val greenLight = Color(0xFFE8F9EE)

// Card Background Colors
val cyanLight3 = Color(0xFFD0F0FD)
val pinkLight3 = Color(0xFFF6D8FE)
val purpleLight3 = Color(0xFFDDD6FE)

// Shadow Colors
val shadowColor = Color(0x1A000000) // 10% black
val shadowColorLight = Color(0x0D000000) // 5% black

// Legacy aliases for backward compatibility
@Deprecated("Use primaryText instead", ReplaceWith("primaryText"))
val primary_text = primaryText

@Deprecated("Use secondaryText instead", ReplaceWith("secondaryText"))
val secondary_text = secondaryText

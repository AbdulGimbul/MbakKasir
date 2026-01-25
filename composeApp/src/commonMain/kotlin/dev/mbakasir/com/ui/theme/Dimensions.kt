package dev.mbakasir.com.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Standardized spacing values for consistent UI spacing throughout the app. Use these values
 * instead of hardcoded dp values for maintainability.
 */
object Spacing {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 48.dp
}

/** Standardized corner radius values for consistent component shapes. */
object CornerRadius {
    val xs = 4.dp
    val sm = 8.dp
    val md = 10.dp
    val lg = 12.dp
    val xl = 16.dp
    val xxl = 24.dp
    val full = 100.dp // For fully rounded (pill) shapes
}

/** Standardized elevation values for consistent shadows. */
object Elevation {
    val none = 0.dp
    val xs = 1.dp
    val sm = 2.dp
    val md = 4.dp
    val lg = 8.dp
    val xl = 12.dp
}

/** Standard icon sizes for consistent iconography. */
object IconSize {
    val xs = 16.dp
    val sm = 20.dp
    val md = 24.dp
    val lg = 32.dp
    val xl = 48.dp
    val xxl = 64.dp
}

/** Standard component heights for touch targets and buttons. */
object ComponentHeight {
    val buttonSmall = 36.dp
    val buttonMedium = 48.dp
    val buttonLarge = 56.dp
    val textFieldDefault = 56.dp
    val listItem = 64.dp
    val appBar = 56.dp
    val bottomNav = 80.dp
}

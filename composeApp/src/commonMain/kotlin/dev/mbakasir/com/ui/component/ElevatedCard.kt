package dev.mbakasir.com.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Elevation
import dev.mbakasir.com.ui.theme.shadowColor
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.ui.theme.surface

/**
 * An elevated card component with consistent shadow styling. Provides subtle press animation and
 * elevation changes on interaction.
 */
@Composable
fun ElevatedCard(
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        containerColor: Color = surface,
        borderColor: Color? = stroke,
        elevation: Dp = Elevation.sm,
        cornerRadius: Dp = CornerRadius.md,
        content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Subtle scale animation when pressed
    val scale by
            animateFloatAsState(
                    targetValue = if (isPressed && onClick != null) 0.98f else 1f,
                    animationSpec = tween(durationMillis = 100),
                    label = "cardScale"
            )

    // Elevation animation when pressed
    val animatedElevation by
            animateDpAsState(
                    targetValue = if (isPressed) Elevation.xs else elevation,
                    animationSpec = tween(durationMillis = 100),
                    label = "cardElevation"
            )

    Card(
            modifier =
                    modifier.fillMaxWidth()
                            .scale(scale)
                            .shadow(
                                    elevation = animatedElevation,
                                    shape = RoundedCornerShape(cornerRadius),
                                    ambientColor = shadowColor,
                                    spotColor = shadowColor
                            )
                            .then(
                                    if (onClick != null) {
                                        Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = onClick
                                        )
                                    } else Modifier
                            ),
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = borderColor?.let { BorderStroke(0.5.dp, it) }
    ) { Box { content() } }
}

/** A flat outlined card without shadow. Use for less prominent card elements. */
@Composable
fun OutlinedCardComponent(
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        containerColor: Color = surface,
        borderColor: Color = stroke,
        cornerRadius: Dp = CornerRadius.md,
        content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by
            animateFloatAsState(
                    targetValue = if (isPressed && onClick != null) 0.98f else 1f,
                    animationSpec = tween(durationMillis = 100),
                    label = "cardScale"
            )

    Card(
            modifier =
                    modifier.fillMaxWidth()
                            .scale(scale)
                            .then(
                                    if (onClick != null) {
                                        Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = onClick
                                        )
                                    } else Modifier
                            ),
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = BorderStroke(1.dp, borderColor)
    ) { Box { content() } }
}

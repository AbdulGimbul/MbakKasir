package dev.mbakasir.com.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import dev.mbakasir.com.ui.component.LottieLoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Elevation
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.disabled
import dev.mbakasir.com.ui.theme.onPrimary
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryDark
import dev.mbakasir.com.ui.theme.primaryLight

@Composable
fun DefaultButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Subtle scale animation when pressed
    val scale by
    animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "buttonScale"
    )

    // Dynamic elevation based on state
    val elevation =
        when {
            !enabled -> Elevation.none
            isPressed -> Elevation.xs
            else -> Elevation.sm
        }

    // Shimmer animation — sweeps continuously
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing)
        ),
        label = "shimmerOffset"
    )

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(primary, primaryLight, primary)
    )

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .scale(scale)
            .shadow(
                elevation = if (enabled) Elevation.sm else Elevation.none,
                shape = RoundedCornerShape(CornerRadius.md),
                ambientColor = primaryDark.copy(alpha = 0.2f),
                spotColor = primaryDark.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(CornerRadius.md))
    ) {
        // Gradient background
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(gradientBrush)
                    .drawWithContent {
                        drawContent()
                        // Shimmer overlay
                        val shimmerWidth = size.width * 0.4f
                        val shimmerStart = shimmerOffset * size.width
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                ),
                                start = Offset(shimmerStart, 0f),
                                end = Offset(shimmerStart + shimmerWidth, size.height)
                            )
                        )
                    }
            )
        }

        Button(
            onClick = { if (!isLoading) onClick() },
            enabled = enabled && !isLoading,
            interactionSource = interactionSource,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = onPrimary,
                    disabledContainerColor = disabled,
                    disabledContentColor = Color.Gray
                ),
            shape = RoundedCornerShape(CornerRadius.md),
            contentPadding = PaddingValues(horizontal = Spacing.xl, vertical = Spacing.md),
            elevation =
                ButtonDefaults.buttonElevation(
                    defaultElevation = Elevation.none,
                    pressedElevation = Elevation.none,
                    disabledElevation = Elevation.none
                ),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    LottieLoadingIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing.sm))
                }
                Text(
                    text = text,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 1.2.em,
                            lineHeightStyle =
                                LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                )
                        ),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            }
        }
    }
}

package dev.mbakasir.com.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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

    Button(
        onClick = { if (!isLoading) onClick() },
        enabled = enabled && !isLoading,
        interactionSource = interactionSource,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = onPrimary,
                disabledContainerColor = disabled,
                disabledContentColor = Color.Gray
            ),
        shape = RoundedCornerShape(CornerRadius.md),
        contentPadding = PaddingValues(horizontal = Spacing.xl, vertical = Spacing.md),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = elevation,
                pressedElevation = Elevation.xs,
                disabledElevation = Elevation.none
            ),
        modifier =
            modifier.defaultMinSize(minHeight = 48.dp)
                .scale(scale)
                .shadow(
                    elevation = if (enabled) Elevation.sm else Elevation.none,
                    shape = RoundedCornerShape(CornerRadius.md),
                    ambientColor = primaryDark.copy(alpha = 0.2f),
                    spotColor = primaryDark.copy(alpha = 0.2f)
                )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = onPrimary,
                    strokeWidth = 2.dp
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
                maxLines = 1
            )
        }
    }
}

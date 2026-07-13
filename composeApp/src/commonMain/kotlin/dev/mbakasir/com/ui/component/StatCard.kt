package dev.mbakasir.com.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Elevation
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primaryGlow
import dev.mbakasir.com.ui.theme.secondaryText
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun StatCard(
    title: String,
    value: String,
    suffix: String? = null,
    imageRes: DrawableResource,
    cardColor: Color,
    cardColorEnd: Color = cardColor
) {
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(cardColor, cardColorEnd)
    )

    Card(
        modifier =
            Modifier.fillMaxWidth()
                .shadow(
                    elevation = Elevation.md,
                    shape = RoundedCornerShape(CornerRadius.lg),
                    ambientColor = primaryGlow,
                    spotColor = primaryGlow
                ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(CornerRadius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.none)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    Modifier.drawBehind {
                        drawRect(brush = backgroundBrush)
                    }
                )
                .padding(vertical = Spacing.xl, horizontal = Spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                    color = secondaryText
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        value,
                        color = dark,
                        style =
                            MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                    )
                    if (suffix != null) {
                        Text(
                            suffix,
                            color = dark.copy(alpha = 0.7f),
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                            modifier = Modifier.padding(start = Spacing.xs, bottom = 2.dp)
                        )
                    }
                }
            }
            Image(
                painterResource(resource = imageRes),
                contentDescription = title,
                modifier = Modifier.padding(end = Spacing.lg).size(72.dp)
            )
        }
    }
}


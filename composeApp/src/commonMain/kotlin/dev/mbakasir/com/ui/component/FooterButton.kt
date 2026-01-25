package dev.mbakasir.com.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.ComponentHeight
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Elevation
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.onPrimary
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryDark
import dev.mbakasir.com.ui.theme.red

@Composable
fun FooterButton(
        onCancelClick: () -> Unit,
        onConfirmClick: () -> Unit,
        cancelText: String,
        confirmText: String,
        borderCancelColor: Color = red,
        contentCancelColor: Color = red,
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
                onClick = onCancelClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = contentCancelColor),
                border = BorderStroke(width = 1.5.dp, color = borderCancelColor),
                shape = RoundedCornerShape(CornerRadius.md),
                modifier =
                        Modifier.weight(1f).defaultMinSize(minHeight = ComponentHeight.buttonMedium)
        ) {
            Text(
                    text = cancelText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(Spacing.xs),
                    maxLines = 1,
                    softWrap = false
            )
        }
        Spacer(modifier = Modifier.width(Spacing.lg))
        Button(
                onClick = onConfirmClick,
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = primary,
                                contentColor = onPrimary
                        ),
                shape = RoundedCornerShape(CornerRadius.md),
                elevation =
                        ButtonDefaults.buttonElevation(
                                defaultElevation = Elevation.sm,
                                pressedElevation = Elevation.xs
                        ),
                modifier =
                        Modifier.weight(1f)
                                .defaultMinSize(minHeight = ComponentHeight.buttonMedium)
                                .shadow(
                                        elevation = Elevation.sm,
                                        shape = RoundedCornerShape(CornerRadius.md),
                                        ambientColor = primaryDark.copy(alpha = 0.2f),
                                        spotColor = primaryDark.copy(alpha = 0.2f)
                                )
        ) {
            Text(
                    text = confirmText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(Spacing.xs),
                    maxLines = 1,
                    softWrap = false
            )
        }
    }
}

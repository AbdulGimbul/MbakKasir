package dev.mbakasir.com.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.blue
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.red
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.strokeLight
import dev.mbakasir.com.ui.theme.yellow
import dev.mbakasir.com.utils.currencyFormat

@Composable
fun SalesItem(
    product: dev.mbakasir.com.features.cashier_role.sales.data.ProductDraftWithItems,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier, accentColor = yellow) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Waktu",
                    tint = secondaryText,
                    modifier = Modifier.padding(end = Spacing.xs)
                )
                Text(
                    text = product.draft.dateTime,
                    color = secondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(Spacing.lg))
            HorizontalDivider(color = strokeLight)
            Spacer(modifier = Modifier.height(Spacing.lg))
            ItemRowSales(
                label = if (product.draft.isPrinted) "Belum posting" else "Draft",
                value = currencyFormat(product.totalAmount.toDouble()),
                color = if (product.draft.isPrinted) yellow else red
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            ItemRowSales(label = product.draft.cashier, value = product.draft.draftId, color = dark)
            Spacer(modifier = Modifier.height(Spacing.lg))
            OutlinedButton(
                onClick = onClick,
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = if (product.draft.isPrinted) blue else primary
                    ),
                border =
                    BorderStroke(
                        width = 1.5.dp,
                        color = if (product.draft.isPrinted) blue else primary
                    ),
                shape = RoundedCornerShape(CornerRadius.xxl),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (product.draft.isPrinted) "Kirim ulang" else "Selesaikan",
                    style =
                        MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    modifier = Modifier.padding(vertical = Spacing.xs)
                )
            }
        }
    }
}

@Composable
fun ItemRowSales(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            modifier = Modifier.weight(1f, fill = false)
        )
        Text(
            text = value,
            color = dark,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2
        )
    }
}

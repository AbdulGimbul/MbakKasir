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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.blue

import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.strokeLight

@Composable
fun HistoryItem(
    date: String,
    method: String,
    total: String,
    cashier: String,
    invoiceNumber: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier, accentColor = blue) {
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
                    text = date,
                    color = secondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = Spacing.xs)
                )
            }
            Spacer(modifier = Modifier.height(Spacing.lg))
            HorizontalDivider(color = strokeLight)
            Spacer(modifier = Modifier.height(Spacing.lg))
            ItemRow(label = method, value = total)
            Spacer(modifier = Modifier.height(Spacing.lg))
            ItemRow(label = cashier, value = invoiceNumber)
            Spacer(modifier = Modifier.height(Spacing.lg))
            OutlinedButton(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = blue),
                border = BorderStroke(width = 1.5.dp, color = blue),
                shape = RoundedCornerShape(CornerRadius.xxl),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Preview",
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
fun ItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = dark,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = value,
            color = dark,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SalesItem(
    product: dev.mbakasir.com.features.cashier_role.sales.data.ProductDraftWithItems,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder(enabled = true),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = product.draft.dateTime,
                    color = dev.mbakasir.com.ui.theme.secondary_text,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            ItemRowSales(
                label = if (product.draft.isPrinted) "Belum posting" else "Draft",
                value = dev.mbakasir.com.utils.currencyFormat(product.totalAmount.toDouble()),
                color = if (product.draft.isPrinted) dev.mbakasir.com.ui.theme.yellow else dev.mbakasir.com.ui.theme.red
            )
            Spacer(modifier = Modifier.height(16.dp))
            ItemRowSales(label = product.draft.cashier, value = product.draft.draftId, color = dev.mbakasir.com.ui.theme.dark)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (product.draft.isPrinted) dev.mbakasir.com.ui.theme.blue else dev.mbakasir.com.ui.theme.primary),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (product.draft.isPrinted) dev.mbakasir.com.ui.theme.blue else dev.mbakasir.com.ui.theme.primary
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (product.draft.isPrinted) "Kirim ulang" else "Selesaikan",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
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
            text = label, color = color,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = value, color = dev.mbakasir.com.ui.theme.dark,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}
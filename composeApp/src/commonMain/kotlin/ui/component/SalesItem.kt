package ui.component

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
import features.cashier_role.sales.domain.ProductTransDraft
import ui.theme.blue
import ui.theme.dark
import ui.theme.primary
import ui.theme.red
import ui.theme.secondary_text
import ui.theme.yellow
import utils.currencyFormat

@Composable
fun SalesItem(
    product: ProductTransDraft,
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
                    text = product.datetime,
                    color = secondary_text,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            ItemRowSales(
                label = if (product.isPrinted) "Belum posting" else "Draft",
                value = currencyFormat(product.totalTagihan.toDouble()),
                color = if (product.isPrinted) yellow else red
            )
            Spacer(modifier = Modifier.height(16.dp))
            ItemRowSales(label = product.kasir, value = product.draftId, color = dark)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (product.isPrinted) blue else primary),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (product.isPrinted) blue else primary
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (product.isPrinted) "Kirim ulang" else "Selesaikan",
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
            text = value, color = dark,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}
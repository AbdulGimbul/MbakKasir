package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import features.cashier_role.sales.domain.ProductTrans
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text
import util.currencyFormat

@Composable
fun EntrySalesItem(
    product: ProductTrans,
    onIncreaseQty: (ProductTrans) -> Unit,
    onDecreaseQty: (ProductTrans) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder(
            enabled = true,
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,

            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "[${product.kode_barang}] ${product.barcode}",
                    color = primary_text,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = product.nama_barang,
                    color = dark,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    currencyFormat(product.harga_item.toDouble()), color = dark,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onDecreaseQty(product) },
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = primary,
                            shape = CircleShape
                        ).size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = primary
                        )
                    }
                    Text(
                        product.qty_jual.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = dark,
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { onIncreaseQty(product) },
                        modifier = Modifier.background(
                            color = primary,
                            shape = CircleShape
                        ).size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
package dev.mbakasir.com.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Elevation
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.icon
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.shadowColor
import dev.mbakasir.com.ui.theme.surface
import dev.mbakasir.com.utils.currencyFormat

@Composable
fun ProductItem(product: ProductEntity, modifier: Modifier = Modifier) {

    Card(
        modifier =
            modifier.fillMaxWidth()
                .shadow(
                    elevation = Elevation.xs,
                    shape = RoundedCornerShape(CornerRadius.md),
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                ),
        shape = RoundedCornerShape(CornerRadius.md),
        border = CardDefaults.outlinedCardBorder(enabled = true),
        colors = CardDefaults.cardColors(containerColor = surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.lg)) {
            Column {
                Text(
                    text = product.barcode,
                    color = primaryText,
                    style =
                        MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                    modifier = Modifier.padding(bottom = Spacing.xs),
                    maxLines = 1
                )
                Text(
                    text = product.namaBarang,
                    color = dark,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(Spacing.xl))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    currencyFormat(product.hargaJual.toDouble()),
                    color = dark,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    Text(
                        text = product.stok,
                        color = icon,
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                        maxLines = 1
                    )
                    Text(
                        text = product.satuan,
                        color = icon,
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

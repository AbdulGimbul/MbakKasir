package dev.mbakasir.com.ui.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.utils.currencyFormat

@Composable
fun EntrySalesItem(
    product: dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity,
    customerType: String = "",
    onIncreaseQty:
        (dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity) -> Unit,
    onDecreaseQty:
        (dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity) -> Unit,
    onQtyChanged:
        (dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity, Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val specialPrice =
        when (customerType) {
            "Pelanggan" -> product.hargaPelanggan
            "Toko" -> product.hargaToko
            "Sales" -> product.hargaSales
            else -> 0
        }

    OutlinedCardComponent(modifier = modifier, accentColor = primary) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.lg)) {
            Column {
                Text(
                    text = "[${product.kodeBarang}] ${product.barcode}",
                    color = primaryText,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    modifier = Modifier.padding(bottom = Spacing.xs)
                )
                Text(
                    text = product.namaBarang,
                    color = dark,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                )
            }
            Spacer(modifier = Modifier.height(Spacing.xl))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (specialPrice > 0 && specialPrice != product.hargaItem) {
                    Column {
                        Text(
                            currencyFormat(product.hargaItem.toDouble()),
                            style =
                                MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration =
                                        androidx.compose.ui.text.style
                                            .TextDecoration.LineThrough
                                ),
                            color = secondaryText
                        )
                        Text(
                            text = currencyFormat(specialPrice.toDouble()),
                            color = dark,
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                        )
                    }
                } else {
                    Text(
                        text = currencyFormat(product.hargaItem.toDouble()),
                        color = dark,
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    IconButton(
                        onClick = { onDecreaseQty(product) },
                        modifier =
                            Modifier.border(
                                width = 1.dp,
                                color = primary,
                                shape = CircleShape
                            )
                                .size(36.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Kurangi jumlah",
                            tint = primary
                        )
                    }

                    val focusManager = LocalFocusManager.current
                    var qtyText by remember(product.qtyJual) {
                        mutableStateOf(product.qtyJual.toString())
                    }

                    BasicTextField(
                        value = qtyText,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 5) {
                                qtyText = newValue
                                val newQty = newValue.toIntOrNull()
                                if (newQty != null && newQty > 0) {
                                    onQtyChanged(product, newQty)
                                }
                            }
                        },
                        textStyle =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = dark,
                                textAlign = TextAlign.Center
                            ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .width(52.dp)
                            .border(
                                width = 1.dp,
                                color = stroke,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    val newQty = qtyText.toIntOrNull()
                                    if (newQty == null || newQty < 1) {
                                        qtyText = product.qtyJual.toString()
                                    }
                                }
                            }
                    )

                    IconButton(
                        onClick = { onIncreaseQty(product) },
                        modifier =
                            Modifier.background(
                                color = primary,
                                shape = CircleShape
                            )
                                .size(36.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah jumlah",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

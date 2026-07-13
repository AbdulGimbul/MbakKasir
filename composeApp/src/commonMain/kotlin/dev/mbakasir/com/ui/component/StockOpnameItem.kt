package dev.mbakasir.com.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
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
import dev.mbakasir.com.ui.theme.icon
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.red
import dev.mbakasir.com.utils.currencyFormat

@Composable
fun StockOpnameItem(
    price: String,
    date: String,
    barcode: String,
    productName: String,
    onPreviewClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCardComponent(modifier = modifier, accentColor = primary) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.md)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        tint = icon,
                        contentDescription = "Waktu",
                        modifier = Modifier.padding(end = Spacing.xs)
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium.copy(color = primaryText)
                    )
                }
                Text(
                    text = currencyFormat(price.toDouble()),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = dark,
                        )
                )
            }
            Spacer(modifier = Modifier.height(Spacing.lg))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Spacing.lg))
            Column {
                Text(
                    text = barcode,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = primaryText
                        )
                )
                Text(
                    text = productName,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            color = dark,
                            fontWeight = FontWeight.SemiBold
                        )
                )
            }
            Spacer(modifier = Modifier.height(Spacing.xs))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onPreviewClick,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = blue,
                            contentColor = Color.White
                        ),
                    shape = RoundedCornerShape(CornerRadius.xxl),
                    modifier = Modifier.weight(1f)
                ) { Text("Preview") }
                Spacer(modifier = Modifier.width(Spacing.md))
                OutlinedButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = red),
                    border = BorderStroke(width = 1.dp, color = red),
                    shape = RoundedCornerShape(CornerRadius.xxl),
                    modifier = Modifier.weight(1f)
                ) { Text("Delete") }
            }
        }
    }
}

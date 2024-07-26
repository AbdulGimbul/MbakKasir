package features.cashier_role

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.component.EntrySalesItem
import ui.theme.dark
import ui.theme.primary
import ui.theme.red
import ui.theme.secondary_text

@Composable
fun EntrySalesScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .imePadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
        ) {
            Text(
                "Entry:",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
                color = dark
            )
            Text(
                "Penjualan",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.SemiBold),
                color = secondary_text,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Scan Barcode", color = secondary_text) },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "Back",
                            tint = primary
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(10) {
                    EntrySalesItem(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
        Column {
            Divider(modifier = Modifier.fillMaxWidth().width(1.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Tagihan", color = dark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Rp. 613.000", color = dark,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = red),
                        border = BorderStroke(
                            width = 1.dp,
                            color = red
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(4.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pembayaran", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
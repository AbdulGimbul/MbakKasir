package dev.mbakasir.com.features.admin_role.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StockOpnamePreviewScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier.clickable { /* handle back navigation */ }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Preview:",
                style = MaterialTheme.typography.titleLarge,
                color = dev.mbakasir.com.ui.theme.dark
            )
            Text(
                "Stock Opname",
                style = MaterialTheme.typography.titleLarge,
                color = dev.mbakasir.com.ui.theme.secondary_text,
            )
            Spacer(modifier = Modifier.height(16.dp))
            StockOpnameDetail(label = "Tanggal:", detail = "2024-06-20 17:07:38")
            StockOpnameDetail(label = "Kode Barang:", detail = "51325113340")
            StockOpnameDetail(label = "Nama Item:", detail = "MR POTATO RS BBQ 60GR")
            StockOpnameDetail(label = "Stok Gudang:", detail = "0")
            StockOpnameDetail(label = "Stok Nyata:", detail = "10")
            StockOpnameDetail(label = "Selisih:", detail = "10")
            StockOpnameDetail(label = "Nilai:", detail = "Rp. 76.790")
            StockOpnameDetail(label = "Keterangan:", detail = "Nyelip")
        }
    }
}

@Composable
fun StockOpnameDetail(label: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = dev.mbakasir.com.ui.theme.dark
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyMedium,
            color = dev.mbakasir.com.ui.theme.primary_text,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
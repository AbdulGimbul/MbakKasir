package dev.mbakasir.com.features.admin_role.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.DefaultButton
import dev.mbakasir.com.ui.component.DisabledTextField
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.secondary_text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryStockOpnameScreen() {
    var barcode by remember { mutableStateOf("") }
    var stokNyata by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Entry:", style = MaterialTheme.typography.titleLarge, color = dark)
            Text(
                "Stock Opname",
                style = MaterialTheme.typography.titleLarge,
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
            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
            Text(
                text = "Nama Barang",
                style = MaterialTheme.typography.titleSmall,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DisabledTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                text = "Stok",
                style = MaterialTheme.typography.titleSmall,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DisabledTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                text = "Stok Nyata",
                style = MaterialTheme.typography.titleSmall,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DefaultTextField(
                value = stokNyata,
                onValueChange = { stokNyata = it },
                placehoder = "Input Stok Nyata",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                text = "Keterangan",
                style = MaterialTheme.typography.titleSmall,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DefaultTextField(
                value = keterangan,
                onValueChange = { keterangan = it },
                placehoder = "Input Keterangan",
                minLines = 2,
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
            )
            DefaultButton(
                text = "Simpan",
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            )
        }
    }
}
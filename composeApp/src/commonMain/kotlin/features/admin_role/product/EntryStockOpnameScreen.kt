package features.admin_role.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.component.DefaultButton
import ui.component.DefaultTextField
import ui.component.DisabledTextField
import ui.theme.dark
import ui.theme.primary
import ui.theme.secondary_text

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
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
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
            Text("Entry:", style = MaterialTheme.typography.h4, color = dark)
            Text(
                "Stock Opname",
                style = MaterialTheme.typography.h4,
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
                style = MaterialTheme.typography.h6,
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
                style = MaterialTheme.typography.h6,
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
                style = MaterialTheme.typography.h6,
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
                style = MaterialTheme.typography.h6,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DefaultTextField(
                value = keterangan,
                onValueChange = { keterangan = it },
                placehoder = "Input Keterangan",
                minLines = 2,
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
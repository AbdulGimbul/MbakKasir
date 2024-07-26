package features.cashier_role

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.component.DefaultTextField
import ui.theme.dark

@Composable
fun SalesScreen() {
    var search by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Penjualan",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
            color = dark,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        DefaultTextField(
            value = search,
            onValueChange = { search = it },
            placehoder = "Search ...",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
    }
}
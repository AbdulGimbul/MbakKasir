package dev.mbakasir.com.features.admin_role.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.StockOpnameItem
import dev.mbakasir.com.ui.theme.dark

@Composable
fun StockOpnameScreen() {
    StockOpname()
}

@Composable
fun StockOpname() {
    var search by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Search")
            }
        },
        modifier = Modifier.statusBarsPadding().navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Stock Opname",
                style = MaterialTheme.typography.titleLarge,
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
            LazyColumn {
                items(10) {
                    StockOpnameItem(modifier = Modifier.padding(vertical = 6.dp))
                }
            }
        }
    }
}
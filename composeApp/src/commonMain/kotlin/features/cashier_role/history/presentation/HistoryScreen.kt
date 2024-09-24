package features.cashier_role.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import features.auth.presentation.EnhancedLoading
import org.koin.compose.viewmodel.koinViewModel
import ui.component.HeadlineText
import ui.component.HistoryItem
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary_text
import ui.theme.stroke

@Composable
fun HistoryScreen(navigateToInvoice: (String) -> Unit) {
    val viewModel = koinViewModel<HistoryViewModel>()
    val history by viewModel.historyResponse.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        EnhancedLoading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .imePadding()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HeadlineText("History", modifier = Modifier.padding(bottom = 32.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodyMedium,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                placeholder = { Text(text = "Search ...", color = secondary_text) },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.FilterAlt,
                            contentDescription = "Filter",
                            tint = primary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = stroke,
                    unfocusedBorderColor = stroke,
                    cursorColor = primary_text,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = secondary_text,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
            LazyColumn {
                history?.let { hist ->
                    items(hist.data) {
                        HistoryItem(
                            date = it.tanggal,
                            method = it.method,
                            total = it.bayar,
                            invoiceNumber = it.invoice,
                            cashier = it.kasir,
                            modifier = Modifier.padding(vertical = 4.dp),
                            onClick = {
                                navigateToInvoice(it.invoice)
                            }
                        )
                    }
                }
            }
        }
    }
}
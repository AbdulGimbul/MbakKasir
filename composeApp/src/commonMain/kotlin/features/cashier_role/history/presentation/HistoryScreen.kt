package features.cashier_role.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import features.auth.presentation.login.EnhancedLoading
import ui.component.HeadlineText
import ui.component.HistoryItem
import ui.navigation.cashier_role.Screen
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary_text
import ui.theme.stroke
import utils.formatDateRange

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    History(uiState = uiState, moveToInvoice = {
        navController.navigate("${Screen.Invoice.route}?noInvoice=$it")
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(uiState: HistoryUiState, moveToInvoice: (String) -> Unit) {

    val state = rememberDateRangePickerState()
    var isDateRangePickerVisible by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("") }

    if (uiState.isLoading) {
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
                value = selectedDateRange,
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
                    IconButton(onClick = { isDateRangePickerVisible = !isDateRangePickerVisible }) {
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

            if (isDateRangePickerVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DatePickerDefaults.colors().containerColor),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            val startDate = state.selectedStartDateMillis?.let { start ->
                                state.selectedEndDateMillis?.let { end ->
                                    formatDateRange(start, end)
                                }
                            }

                            startDate?.also { range ->
                                selectedDateRange = range
                            }
                            isDateRangePickerVisible = false
                        },
                        enabled = state.selectedEndDateMillis != null
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "")
                    }
                }

                DateRangePicker(
                    state = state,
                    showModeToggle = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn {
                uiState.history?.let { hist ->
                    items(hist.data) {
                        HistoryItem(
                            date = it.tanggal,
                            method = it.method,
                            total = it.bayar,
                            invoiceNumber = it.invoice,
                            cashier = it.kasir,
                            modifier = Modifier.padding(vertical = 4.dp),
                            onClick = {
                                moveToInvoice(it.invoice)
                            }
                        )
                    }
                }
            }
        }
    }
}
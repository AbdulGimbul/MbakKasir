package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    History(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        moveToInvoice = {
            navController.navigate("${dev.mbakasir.com.ui.navigation.cashier_role.Screen.Invoice.route}?noInvoice=$it")
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit,
    moveToInvoice: (String) -> Unit
) {

    val state = rememberDateRangePickerState()
    var isDateRangePickerVisible by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("") }

    if (uiState.isLoading) {
        dev.mbakasir.com.features.auth.presentation.login.EnhancedLoading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .imePadding()
                .statusBarsPadding()
        ) {
            dev.mbakasir.com.ui.component.HeadlineText(
                "History",
                modifier = Modifier.padding(bottom = 32.dp)
            )
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
                placeholder = { Text(text = "Search ...", color = dev.mbakasir.com.ui.theme.secondary_text) },
                trailingIcon = {
                    IconButton(onClick = { isDateRangePickerVisible = !isDateRangePickerVisible }) {
                        Icon(
                            Icons.Outlined.FilterAlt,
                            contentDescription = "Filter",
                            tint = dev.mbakasir.com.ui.theme.primary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = dev.mbakasir.com.ui.theme.stroke,
                    unfocusedBorderColor = dev.mbakasir.com.ui.theme.stroke,
                    cursorColor = dev.mbakasir.com.ui.theme.primary_text,
                    focusedLabelColor = dev.mbakasir.com.ui.theme.primary,
                    unfocusedLabelColor = dev.mbakasir.com.ui.theme.secondary_text,
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
                            val startDate = state.selectedStartDateMillis
                            val endDate = state.selectedEndDateMillis

                            if (startDate != null && endDate != null) {
                                selectedDateRange =
                                    dev.mbakasir.com.utils.formatDateRange(startDate, endDate)

                                val apiStartDate =
                                    dev.mbakasir.com.utils.formatDateForApi(startDate)
                                val apiEndDate = dev.mbakasir.com.utils.formatDateForApi(endDate)

                                onEvent(HistoryUiEvent.GetHistories(apiStartDate, apiEndDate))

                                isDateRangePickerVisible = false
                            }
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
                        dev.mbakasir.com.ui.component.HistoryItem(
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
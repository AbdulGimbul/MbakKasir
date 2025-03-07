package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.ui.component.HeadlineText
import dev.mbakasir.com.ui.component.HistoryItem
import dev.mbakasir.com.ui.navigation.cashier_role.CashierScreen
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.utils.formatDateForApi
import dev.mbakasir.com.utils.formatDateRange

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val historyCount = uiState.history?.data?.size ?: 0
                if (lastVisibleIndex != null) {
                    if (lastVisibleIndex >= historyCount - 1 && historyCount > 0) {
                        viewModel.onEvent(HistoryUiEvent.GetHistories)
                    }
                }
            }
    }

    History(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        moveToInvoice = {
            navController.navigate("${CashierScreen.Invoice.route}?noInvoice=$it")
        },
        listState = listState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit,
    moveToInvoice: (String) -> Unit,
    listState: LazyListState
) {

    val state = rememberDateRangePickerState()
    var isDateRangePickerVisible by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .imePadding()
            .statusBarsPadding()
    ) {
        HeadlineText(
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
                        val startDate = state.selectedStartDateMillis
                        val endDate = state.selectedEndDateMillis

                        if (startDate != null && endDate != null) {
                            selectedDateRange =
                                formatDateRange(startDate, endDate)

                            val apiStartDate =
                                formatDateForApi(startDate)
                            val apiEndDate = formatDateForApi(endDate)

                            onEvent(HistoryUiEvent.UpdateDate(apiStartDate, apiEndDate))
                            onEvent(HistoryUiEvent.GetHistories)

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

        LazyColumn(state = listState) {
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

                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
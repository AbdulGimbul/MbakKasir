package dev.mbakasir.com.features.admin_role.stock_opname.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import dev.mbakasir.com.ui.component.StockOpnameItem
import dev.mbakasir.com.ui.navigation.admin_role.AdminScreen
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.utils.formatDateForApi
import dev.mbakasir.com.utils.formatDateRange

@Composable
fun StockOpnameScreen(viewModel: StockOpnameViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val stockOpnameCount = state.stockOpname?.data?.size ?: 0
                if (lastVisibleIndex != null) {
                    if (lastVisibleIndex >= stockOpnameCount - 1 && stockOpnameCount > 0) {
                        viewModel.onEvent(StockOpnameUiEvent.GetStockOpname)
                    }
                }
            }
    }

    StockOpname(
        uiState = state,
        onEvent = viewModel::onEvent,
        moveToEntryStcokOpname = {
            navController.navigate(AdminScreen.EntryStockOpname.route)
        },
        listState = listState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockOpname(
    uiState: StockOpnameUiState,
    onEvent: (StockOpnameUiEvent) -> Unit,
    moveToEntryStcokOpname: () -> Unit,
    listState: LazyListState
) {
    val state = rememberDateRangePickerState()
    var isDateRangePickerVisible by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("") }

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
                .padding(bottom = 24.dp),
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

                            onEvent(StockOpnameUiEvent.UpdateDate(apiStartDate, apiEndDate))
                            onEvent(StockOpnameUiEvent.GetStockOpname)

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
            uiState.stockOpname?.let { stock ->
                items(stock.data) {
                    StockOpnameItem(
                        price = it.nilai,
                        date = it.tanggal,
                        barcode = it.namaBarang,
                        onClick = {},
                        productName = it.keterangan
                    )
                }
            }
        }
    }
}
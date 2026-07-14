package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesUiEvent
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesUiState
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.SummaryRow
import dev.mbakasir.com.ui.component.FooterButton
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.utils.currencyFormat
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.cancel
import mbakkasir.composeapp.generated.resources.choose_customer
import mbakkasir.composeapp.generated.resources.customer_label
import mbakkasir.composeapp.generated.resources.customer_not_found
import mbakkasir.composeapp.generated.resources.discount
import mbakkasir.composeapp.generated.resources.payment
import mbakkasir.composeapp.generated.resources.scan_barcode_label
import mbakkasir.composeapp.generated.resources.search_customer
import mbakkasir.composeapp.generated.resources.total_bill
import mbakkasir.composeapp.generated.resources.total_price
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchSection(
    uiState: EntrySalesUiState,
    draftId: String,
    onEvent: (EntrySalesUiEvent) -> Unit
) {
    val expanded = uiState.searchResults.isNotEmpty()

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {}) {
        OutlinedTextField(
            value = uiState.inputUser,
            onValueChange = { newBarcode ->
                onEvent(EntrySalesUiEvent.OnInputUserChanged(newBarcode))
                onEvent(EntrySalesUiEvent.SearchProduct)
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    stringResource(Res.string.scan_barcode_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryText
                )
            },
            trailingIcon = {
                IconButton(onClick = { onEvent(EntrySalesUiEvent.ScanIconClick) }) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = "QR",
                        tint = primary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = stroke,
                    unfocusedBorderColor = stroke,
                    cursorColor = primaryText,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = secondaryText,
                ),
            modifier =
                Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { },
            modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
        ) {
            uiState.searchResults.forEach { product ->
                val displayText =
                    when {
                        product.barcode.contains(
                            uiState.inputUser,
                            ignoreCase = true
                        ) -> product.barcode

                        product.namaBarang.contains(
                            uiState.inputUser,
                            ignoreCase = true
                        ) -> product.namaBarang

                        product.kodeBarang.contains(
                            uiState.inputUser,
                            ignoreCase = true
                        ) -> product.kodeBarang

                        else -> ""
                    }

                if (displayText.isNotEmpty()) {
                    DropdownMenuItem(
                        onClick = {
                            onEvent(
                                EntrySalesUiEvent.ScanProduct(
                                    draftId,
                                    product.barcode
                                )
                            )
                            onEvent(
                                EntrySalesUiEvent
                                    .OnInputUserChanged("")
                            )
                            onEvent(EntrySalesUiEvent.SearchProduct)
                        },
                        text = { Text(displayText) },
                        contentPadding =
                            ExposedDropdownMenuDefaults
                                .ItemContentPadding
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSection(uiState: EntrySalesUiState, onEvent: (EntrySalesUiEvent) -> Unit) {
    val (allowCustExpanded, setCustExpanded) = remember { mutableStateOf(false) }
    val custExpanded = allowCustExpanded && uiState.customers.isNotEmpty()
    val (custFilter, setCustFilter) = remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.customer_label),
                style =
                    MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                color = dark,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (uiState.searchCust.isNotEmpty()) {
                    Text(
                        text =
                            uiState.customers
                                .find {
                                    it.kode ==
                                            uiState.searchCust
                                }
                                ?.nama
                                ?: stringResource(
                                    Res.string
                                        .customer_not_found
                                ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = primary,
                        modifier =
                            Modifier.clickable { setCustExpanded(true) }
                    )
                    IconButton(
                        onClick = {
                            onEvent(
                                EntrySalesUiEvent
                                    .OnSearchCustChanged("")
                            )
                        },
                        modifier = Modifier.height(32.dp).width(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear customer",
                            tint = primary
                        )
                    }
                } else {
                    Text(
                        text = stringResource(Res.string.choose_customer),
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryText,
                        modifier =
                            Modifier.clickable { setCustExpanded(true) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (custExpanded) {
            ExposedDropdownMenuBox(
                expanded = custExpanded,
                onExpandedChange = setCustExpanded
            ) {
                OutlinedTextField(
                    value = custFilter,
                    onValueChange = { setCustFilter(it) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = {
                        Text(
                            stringResource(Res.string.search_customer),
                            style = MaterialTheme.typography.bodyMedium,
                            color = secondaryText
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = stroke,
                            unfocusedBorderColor = stroke,
                            cursorColor = primaryText,
                            focusedLabelColor = primary,
                            unfocusedLabelColor = secondaryText,
                        ),
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .menuAnchor(
                                type =
                                    MenuAnchorType
                                        .PrimaryEditable
                            )
                )

                ExposedDropdownMenu(
                    expanded = custExpanded,
                    onDismissRequest = { setCustExpanded(false) },
                    modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
                ) {
                    uiState.customers
                        .filter {
                            it.nama.contains(
                                custFilter,
                                ignoreCase = true
                            ) ||
                                    it.kode.contains(
                                        custFilter,
                                        ignoreCase = true
                                    )
                        }
                        .forEach { customer ->
                            DropdownMenuItem(
                                onClick = {
                                    onEvent(
                                        EntrySalesUiEvent
                                            .OnSearchCustChanged(
                                                customer.kode
                                            )
                                    )
                                    setCustExpanded(false)
                                    setCustFilter("")
                                },
                                text = {
                                    Column {
                                        Row(
                                            modifier =
                                                Modifier.fillMaxWidth(),
                                            verticalAlignment =
                                                Alignment
                                                    .CenterVertically,
                                            horizontalArrangement =
                                                Arrangement
                                                    .SpaceBetween
                                        ) {
                                            Text(
                                                text =
                                                    customer.nama,
                                                style =
                                                    MaterialTheme
                                                        .typography
                                                        .titleSmall
                                            )
                                            Text(
                                                text =
                                                    customer.kode,
                                                style =
                                                    MaterialTheme
                                                        .typography
                                                        .bodyMedium
                                            )
                                        }

                                        Text(
                                            text =
                                                customer.alamat,
                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .bodySmall
                                        )
                                    }
                                },
                                contentPadding =
                                    ExposedDropdownMenuDefaults
                                        .ItemContentPadding
                            )
                        }
                }
            }
        }
    }
}

@Composable
fun TransactionSummaryFooter(
    uiState: EntrySalesUiState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column {
        SummaryRow(
            label = stringResource(Res.string.total_price),
            value = currencyFormat(uiState.totalHarga.toDouble())
        )
        SummaryRow(
            label = stringResource(Res.string.discount),
            value = currencyFormat(uiState.totalDiskon.toDouble())
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        SummaryRow(
            label = stringResource(Res.string.total_bill),
            value = currencyFormat(uiState.totalTagihan.toDouble()),
            isBold = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        FooterButton(
            onCancelClick = onCancel,
            onConfirmClick = onConfirm,
            cancelText = stringResource(Res.string.cancel),
            confirmText = stringResource(Res.string.payment)
        )
    }
}

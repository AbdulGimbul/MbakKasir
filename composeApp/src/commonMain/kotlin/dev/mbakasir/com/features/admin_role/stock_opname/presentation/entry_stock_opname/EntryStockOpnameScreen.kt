package dev.mbakasir.com.features.admin_role.stock_opname.presentation.entry_stock_opname

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.ui.component.DefaultButton
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.DisabledTextField
import dev.mbakasir.com.ui.component.HeadlineText
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.ui.theme.stroke

@Composable
fun EntryStockOpnameScreen(viewModel: EntryStockOpnameViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EntryStockOpname(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        navigateBack = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryStockOpname(
    uiState: EntryStockOpnameUiState,
    onEvent: (EntryStockOpnameUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && uiState.searchResults.isNotEmpty()

    LaunchedEffect(uiState.updateStockResponse) {
        uiState.updateStockResponse?.let {
            if (it.code == "200") navigateBack()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HeadlineText("Entry:")
        Spacer(modifier = Modifier.width(8.dp))
        HeadlineText(
            text = "Stock Opname",
            color = primary_text,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = setExpanded
        ) {
            OutlinedTextField(
                value = uiState.inputUser,
                onValueChange = { newBarcode ->
                    onEvent(EntryStockOpnameUiEvent.OnInputUserChanged(newBarcode))
                    onEvent(EntryStockOpnameUiEvent.SearchProduct)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(
                        "Scan Barcode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondary_text
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onEvent(EntryStockOpnameUiEvent.ScanIconClick)
                    }) {
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = stroke,
                    unfocusedBorderColor = stroke,
                    cursorColor = primary_text,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = secondary_text,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    setExpanded(false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                uiState.searchResults.forEach { product ->
                    val displayText = when {
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
                                onEvent(EntryStockOpnameUiEvent.OnInputUserChanged(product.barcode))
                                onEvent(
                                    EntryStockOpnameUiEvent.ScanProduct(
                                        product.barcode
                                    )
                                )
                                setExpanded(false)
                            },
                            text = {
                                Text(displayText)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
        Text(
            text = "Nama Barang",
            style = MaterialTheme.typography.titleSmall,
            color = dark,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DisabledTextField(
            value = uiState.product.namaBarang,
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
            value = uiState.product.stok,
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
            value = uiState.product.jumlah,
            onValueChange = { onEvent(EntryStockOpnameUiEvent.OnJumlahChanged(it)) },
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
            value = uiState.product.keterangan,
            onValueChange = { onEvent(EntryStockOpnameUiEvent.OnKeteranganChanged(it)) },
            placehoder = "Input Keterangan",
            minLines = 2,
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
        } else {
            DefaultButton(
                text = "Simpan",
                onClick = { onEvent(EntryStockOpnameUiEvent.SubmitUpdateStock) },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            )
        }
    }
}
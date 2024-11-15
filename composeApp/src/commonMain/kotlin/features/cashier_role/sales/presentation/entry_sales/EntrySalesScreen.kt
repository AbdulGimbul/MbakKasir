package features.cashier_role.sales.presentation.entry_sales

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import features.auth.presentation.login.EnhancedLoading
import features.cashier_role.sales.domain.toSerializable
import features.cashier_role.sales.presentation.payment.PaymentOptions
import features.cashier_role.sales.presentation.payment.PaymentUiEvent
import features.cashier_role.sales.presentation.payment.PaymentUiState
import features.cashier_role.sales.presentation.payment.PaymentViewModel
import features.cashier_role.sales.presentation.payment.SummaryRow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import qrscanner.QrScanner
import rememberMessageBarState
import ui.component.DefaultTextField
import ui.component.DisabledTextField
import ui.component.EntrySalesItem
import ui.component.FooterButton
import ui.component.HeadlineText
import ui.navigation.cashier_role.MbakKasirNavigationType
import ui.navigation.cashier_role.Screen
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary_text
import ui.theme.stroke
import utils.currencyFormat

@Composable
fun EntrySalesScreen(
    viewModel: EntrySalesViewModel,
    paymentViewModel: PaymentViewModel,
    navController: NavController,
    navigationType: MbakKasirNavigationType,
    draftId: String,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val paymentUiState by paymentViewModel.uiState.collectAsStateWithLifecycle()

    if (navigationType == MbakKasirNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        EntrySalesAndPayment(
            entryUiState = uiState,
            paymentUiState = paymentUiState,
            entryOnEvent = { viewModel.onEvent(it) },
            paymentOnEvent = { paymentViewModel.onEvent(it) },
            navigateBack = { navController.navigateUp() }
        )
    } else {
        EntrySales(
            uiState = uiState,
            onEvent = { viewModel.onEvent(it) },
            moveToPayment = { scannedProducts, draftId ->
                navController.navigate("${Screen.Payment.route}/?scannedProducts=$scannedProducts&draftId=$draftId") {
                    restoreState = true
                }
            },
            navigateBack = {
                navController.navigateUp()
            },
            draftId = draftId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrySales(
    uiState: EntrySalesUiState,
    onEvent: (EntrySalesUiEvent) -> Unit,
    moveToPayment: (String, String) -> Unit,
    navigateBack: () -> Unit,
    draftId: String
) {

    val showDialog = remember { mutableStateOf(false) }
    val state = rememberMessageBarState()
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && uiState.searchResults.isNotEmpty()

    LaunchedEffect(draftId) {
        if (draftId != null) {
            onEvent(EntrySalesUiEvent.LoadScannedProducts(draftId))
        }
    }

    LaunchedEffect(uiState.scannedProducts) {
        val totalTagihan = uiState.scannedProducts.sumOf { it.harga_item * it.qty_jual }
        onEvent(EntrySalesUiEvent.OnTotalTagihanChanged(totalTagihan))
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            state.addError(Exception(uiState.errorMessage))
        }
    }

    ContentWithMessageBar(
        messageBarState = state,
        errorMaxLines = 2,
        showCopyButton = false,
        visibilityDuration = 3000L,
        errorContainerColor = Color.Yellow,
        modifier = Modifier.statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    HeadlineText("Tambah")
                    Spacer(modifier = Modifier.width(8.dp))
                    HeadlineText(
                        text = "Penjualan",
                        color = primary_text
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = setExpanded
                ) {
                    OutlinedTextField(
                        value = uiState.inputUser,
                        onValueChange = { newBarcode ->
                            onEvent(EntrySalesUiEvent.OnInputUserChanged(newBarcode))
                            if (newBarcode.length >= 5) {
                                onEvent(EntrySalesUiEvent.SearchProduct)
                            }
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
                                onEvent(EntrySalesUiEvent.ScanIconClick)
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

                                product.nama_barang.contains(
                                    uiState.inputUser,
                                    ignoreCase = true
                                ) -> product.nama_barang

                                product.kode_barang.contains(
                                    uiState.inputUser,
                                    ignoreCase = true
                                ) -> product.kode_barang

                                else -> ""
                            }

                            if (displayText.isNotEmpty()) {
                                DropdownMenuItem(
                                    onClick = {
                                        onEvent(EntrySalesUiEvent.OnInputUserChanged(product.barcode))
                                        onEvent(
                                            EntrySalesUiEvent.ScanProduct(
                                                draftId.toString(),
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

                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(uiState.scannedProducts) { product ->
                        EntrySalesItem(
                            product = product,
                            onIncreaseQty = {
                                onEvent(
                                    EntrySalesUiEvent.IncreaseProductQty(
                                        draftId.toString(),
                                        it
                                    )
                                )
                            },
                            onDecreaseQty = {
                                onEvent(
                                    EntrySalesUiEvent.DecreaseProductQty(
                                        draftId.toString(),
                                        it
                                    )
                                )
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            if (uiState.startBarCodeScan) {
                QrScanner(
                    modifier = Modifier
                        .clipToBounds()
                        .clip(shape = RoundedCornerShape(size = 14.dp)),
                    flashlightOn = uiState.flashlightOn,
                    onCompletion = {
                        onEvent(EntrySalesUiEvent.OnInputUserChanged(it))
                        onEvent(EntrySalesUiEvent.ScanIconClick)
                        onEvent(EntrySalesUiEvent.ScanProduct(draftId.toString(), it))
                    },
                    onFailure = {
                        if (it.isEmpty()) {
                            state.addError(Exception("Invalid qr code"))
                        } else {
                            state.addError(Exception(it))
                        }
                    },
                    openImagePicker = false,
                    imagePickerHandler = { onEvent(EntrySalesUiEvent.OnLaunchGallery(it)) },
                )
            }

            Column {
                HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Tagihan", color = dark,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            currencyFormat(uiState.totalTagihan.toDouble()), color = dark,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    FooterButton(
                        onCancelClick = {
                            showDialog.value = true
                        },
                        onConfirmClick = {
                            if (uiState.scannedProducts.isEmpty()) {
                                state.addError(Exception("Ekhm, barangnya ditambahkan dulu ya!"))
                                return@FooterButton
                            }
                            val scannedProductsJson =
                                Json.encodeToString(uiState.scannedProducts.map { it.toSerializable() })
                            moveToPayment(scannedProductsJson, draftId)
                        },
                        cancelText = "Batal",
                        confirmText = "Pembayaran"
                    )
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onEvent(EntrySalesUiEvent.DeleteProduct(draftId.toString()))
                        navigateBack()
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Tidak")
                }
            },
            title = {
                Text(
                    text = "Batalkan Transaksi",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "Apakah anda yakini? Data yang sudah ditambahkan akan hilang!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrySalesAndPayment(
    entryUiState: EntrySalesUiState,
    paymentUiState: PaymentUiState,
    entryOnEvent: (EntrySalesUiEvent) -> Unit,
    paymentOnEvent: (PaymentUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val messageBarState = rememberMessageBarState()
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && entryUiState.searchResults.isNotEmpty()

    LaunchedEffect(entryUiState.scannedProducts) {
        val totalTagihan = entryUiState.scannedProducts.sumOf { it.harga_item * it.qty_jual }
        entryOnEvent(EntrySalesUiEvent.OnTotalTagihanChanged(totalTagihan))
    }

    LaunchedEffect(entryUiState.errorMessage) {
        entryUiState.errorMessage?.let {
            messageBarState.addError(Exception(entryUiState.errorMessage))
        }
    }

    Row(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp) // Row to place EntrySales and Payment side by side
    ) {
        // Left Side: Entry Sales
        ContentWithMessageBar(
            messageBarState = messageBarState,
            modifier = Modifier
                .weight(1f)  // Takes 50% of the screen width
                .fillMaxHeight()
                .padding(end = 8.dp)  // Padding to separate from Payment
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                // Entry Sales Section
                Column(modifier = Modifier.weight(1f)) {
                    HeadlineText("Entry:")
                    HeadlineText(
                        text = "Penjualan",
                        color = secondary_text,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = setExpanded
                    ) {
                        OutlinedTextField(
                            value = entryUiState.inputUser,
                            onValueChange = { newBarcode ->
                                entryOnEvent(EntrySalesUiEvent.OnInputUserChanged(newBarcode))
                                if (newBarcode.length >= 5) {
                                    entryOnEvent(EntrySalesUiEvent.SearchProduct)
                                }
                            },
                            label = { Text("Scan Barcode") },
                            trailingIcon = {
                                IconButton(onClick = { entryOnEvent(EntrySalesUiEvent.ScanIconClick) }) {
                                    Icon(Icons.Default.QrCodeScanner, contentDescription = "QR")
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { setExpanded(false) }
                        ) {
                            entryUiState.searchResults.forEach { product ->
                                val displayText = when {
                                    product.barcode.contains(
                                        entryUiState.inputUser,
                                        ignoreCase = true
                                    ) -> product.barcode

                                    product.nama_barang.contains(
                                        entryUiState.inputUser,
                                        ignoreCase = true
                                    ) -> product.nama_barang

                                    product.kode_barang.contains(
                                        entryUiState.inputUser,
                                        ignoreCase = true
                                    ) -> product.kode_barang

                                    else -> ""
                                }

                                if (displayText.isNotEmpty()) {
                                    DropdownMenuItem(
                                        onClick = {
                                            entryOnEvent(
                                                EntrySalesUiEvent.OnInputUserChanged(
                                                    product.barcode
                                                )
                                            )
//                                            entryOnEvent(EntrySalesUiEvent.ScanProduct(draftId.toString(), product.barcode))
                                            setExpanded(false)
                                        },
                                        text = { Text(displayText) }
                                    )
                                }
                            }
                        }
                    }

                    LazyColumn {
//                        items(entryUiState.scannedProducts) { product ->
//                            EntrySalesItem(
//                                product = product,
//                                onIncreaseQty = {
//                                    entryOnEvent(
//                                        EntrySalesUiEvent.IncreaseProductQty(
//                                            it
//                                        )
//                                    )
//                                },
//                                onDecreaseQty = {
//                                    entryOnEvent(
//                                        EntrySalesUiEvent.DecreaseProductQty(
//                                            it
//                                        )
//                                    )
//                                },
//                                modifier = Modifier.padding(vertical = 4.dp)
//                            )
//                        }
                    }

                    if (entryUiState.startBarCodeScan) {
                        QrScanner(
                            modifier = Modifier
                                .clipToBounds()
                                .clip(shape = RoundedCornerShape(size = 14.dp)),
                            flashlightOn = entryUiState.flashlightOn,
                            onCompletion = {
                                entryOnEvent(EntrySalesUiEvent.OnInputUserChanged(it))
                                entryOnEvent(EntrySalesUiEvent.ScanIconClick)
//                                entryOnEvent(EntrySalesUiEvent.ScanProduct(it))
                            },
                            onFailure = {
                                if (it.isEmpty()) {
                                    messageBarState.addError(Exception("Invalid qr code"))
                                } else {
                                    messageBarState.addError(Exception(it))
                                }
                            },
                            openImagePicker = false,
                            imagePickerHandler = { entryOnEvent(EntrySalesUiEvent.OnLaunchGallery(it)) },
                        )
                    }
                }
            }
        }

        // Right Side: Payment Section
        val radioOptions = listOf("Tunai", "Kredit")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        val paymentMessageBarState = rememberMessageBarState()

        LaunchedEffect(paymentUiState.errorMessage) {
            paymentUiState.errorMessage?.let {
                paymentMessageBarState.addError(Exception("Ups, terjadi kesalahan!"))
            }
        }

        LaunchedEffect(paymentUiState.isConnected) {
            if (!paymentUiState.isConnected) {
                paymentMessageBarState.addError(Exception("Awas, internetmu mati!"))
            }
        }

        ContentWithMessageBar(
            messageBarState = paymentMessageBarState,
            modifier = Modifier
                .weight(1f)  // Takes 50% of the screen width
                .fillMaxHeight()
                .padding(start = 8.dp)  // Padding to separate from Entry Sales
        ) {
            if (paymentUiState.isLoading) {
                EnhancedLoading()
            } else {
                Column(modifier = Modifier.fillMaxHeight()) {
                    // Payment Section
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        HeadlineText(
                            text = "Pembayaran",
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        Text("Jenis Pembayaran")
                        PaymentOptions(
                            radioOptions = radioOptions,
                            selectedOption = selectedOption,
                            onOptionSelected = onOptionSelected
                        )

                        if (selectedOption == "Kredit") {
                            OutlinedTextField(
                                value = paymentUiState.selectedDate,
                                onValueChange = {},
                                label = { Text("Jatuh Tempo") },
                                trailingIcon = {
                                    IconButton(onClick = { paymentOnEvent(PaymentUiEvent.DateIconClicked) }) {
                                        Icon(Icons.Default.DateRange, contentDescription = "Date")
                                    }
                                },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            )
                        }

                        DefaultTextField(
                            value = paymentUiState.uangDiterima,
                            onValueChange = { paymentOnEvent(PaymentUiEvent.UangDiterimaChanged(it)) },
                            placehoder = "Nominal Uang",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )

                        DisabledTextField(
                            value = paymentUiState.kembalian.toString(),
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )
                    }

                    // Payment Summary
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Column(
                        modifier = Modifier.imePadding()
                    ) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            SummaryRow(
                                label = "Total Harga:",
                                value = currencyFormat(paymentUiState.totalHarga.toDouble()),
                            )
                            SummaryRow(
                                label = "Diskon:",
                                value = currencyFormat(paymentUiState.diskon.toDouble())
                            )
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().width(1.dp)
                                    .padding(vertical = 10.dp)
                            )
                            SummaryRow(
                                label = "Totlal Tagihan",
                                value = currencyFormat(paymentUiState.subtotal.toDouble()),
                                isBold = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FooterButton(
                                onCancelClick = navigateBack,
                                onConfirmClick = {
                                    if (paymentUiState.uangDiterima.isEmpty() || paymentUiState.uangDiterima.toInt() < paymentUiState.subtotal) {
                                        paymentMessageBarState.addError(Exception("Hei, uang diterima tidak bisa kurang dari total harga!"))
                                        return@FooterButton
                                    }
                                    val method = if (selectedOption == "Tunai") "Cash" else "Kredit"
                                    paymentOnEvent(PaymentUiEvent.ConfirmButtonClicked)
                                },
                                cancelText = "Kembali",
                                confirmText = "Bayar"
                            )
                        }
                    }
                }
            }
        }
    }
}
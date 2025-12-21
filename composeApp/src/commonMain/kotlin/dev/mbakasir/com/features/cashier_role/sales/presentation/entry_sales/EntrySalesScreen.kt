package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales

import ContentWithMessageBar
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.features.cashier_role.sales.domain.toSerializable
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentOptions
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiEvent
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.SummaryRow
import dev.mbakasir.com.ui.component.CurrencyVisualTransformation
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.DisabledTextField
import dev.mbakasir.com.ui.component.EnhancedLoading
import dev.mbakasir.com.ui.component.EntrySalesItem
import dev.mbakasir.com.ui.component.FooterButton
import dev.mbakasir.com.ui.component.formatCurrencyInput
import dev.mbakasir.com.ui.component.HeadlineText
import dev.mbakasir.com.ui.navigation.cashier_role.CashierScreen
import dev.mbakasir.com.ui.navigation.cashier_role.MbakKasirNavigationType
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.ui.theme.stroke
import kotlinx.serialization.json.Json
import qrscanner.CameraLens
import qrscanner.QrScanner
import rememberMessageBarState

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
            moveToPayment = { scannedProducts, draftId, customerCode ->
                navController.navigate("${CashierScreen.Payment.route}/?scannedProducts=$scannedProducts&draftId=$draftId&customerCode=$customerCode") {
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
    moveToPayment: (String, String, String) -> Unit,
    navigateBack: () -> Unit,
    draftId: String
) {

    val showDialog = remember { mutableStateOf(false) }
    val state = rememberMessageBarState()
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && uiState.searchResults.isNotEmpty()
    val (allowCustExpanded, setCustExpanded) = remember { mutableStateOf(false) }
    val custExpanded = allowCustExpanded && uiState.customers.isNotEmpty()

    LaunchedEffect(draftId) {
        if (draftId != null) {
            onEvent(EntrySalesUiEvent.LoadScannedProducts(draftId))
        }
    }

    LaunchedEffect(uiState.scannedProducts, uiState.searchCust) {
        val customer = uiState.customers.find { it.kode == uiState.searchCust }
        val customerType = customer?.jenis_cs ?: ""
        var totalHarga = 0
        var totalDiskon = 0

        uiState.scannedProducts.forEach { product ->
            val specialPrice = when (customerType) {
                "Pelanggan" -> if (product.hargaPelanggan > 0) product.hargaPelanggan else product.hargaItem
                "Toko" -> if (product.hargaToko > 0) product.hargaToko else product.hargaItem
                "Sales" -> if (product.hargaSales > 0) product.hargaSales else product.hargaItem
                else -> product.hargaItem
            }
            val originalPrice = product.hargaItem
            val qty = product.qtyJual
            val gross = originalPrice * qty
            val discount = (originalPrice - specialPrice) * qty

            totalHarga += gross
            totalDiskon += discount
        }
        val totalTagihan = totalHarga - totalDiskon
        onEvent(EntrySalesUiEvent.OnTotalsChanged(totalHarga, totalDiskon, totalTagihan))
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
                            onEvent(EntrySalesUiEvent.SearchProduct)
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
                        val customer = uiState.customers.find { it.kode == uiState.searchCust }
                        val customerType = customer?.jenis_cs ?: ""
                        EntrySalesItem(
                            product = product,
                            customerType = customerType,
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
                    cameraLens = CameraLens.Back
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
                            text = "Pelanggan",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = dark,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (uiState.searchCust.isNotEmpty()) {
                                Text(
                                    text = uiState.searchCust,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = primary,
                                    modifier = Modifier.clickable { setCustExpanded(true) }
                                )
                                IconButton(
                                    onClick = {
                                        onEvent(EntrySalesUiEvent.OnSearchCustChanged(""))
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
                                    text = "Pilih Pelanggan",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondary_text,
                                    modifier = Modifier.clickable { setCustExpanded(true) }
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
                                value = "",
                                onValueChange = {},
                                textStyle = MaterialTheme.typography.bodyMedium,
                                label = {
                                    Text(
                                        "Cari Pelanggan",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = secondary_text
                                    )
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
                                expanded = custExpanded,
                                onDismissRequest = {
                                    setCustExpanded(false)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                            ) {
                                uiState.customers.forEach { customer ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onEvent(EntrySalesUiEvent.OnSearchCustChanged(customer.kode))
                                            setCustExpanded(false)
                                        },
                                        text = {
                                            Column {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = customer.nama,
                                                        style = MaterialTheme.typography.titleSmall
                                                    )
                                                    Text(
                                                        text = customer.kode,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }

                                                Text(
                                                    text = customer.alamat,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SummaryRow(
                        label = "Total Harga",
                        value = dev.mbakasir.com.utils.currencyFormat(uiState.totalHarga.toDouble())
                    )
                    SummaryRow(
                        label = "Diskon",
                        value = dev.mbakasir.com.utils.currencyFormat(uiState.totalDiskon.toDouble())
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SummaryRow(
                        label = "Total Tagihan",
                        value = dev.mbakasir.com.utils.currencyFormat(uiState.totalTagihan.toDouble()),
                        isBold = true
                    )
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
                            val customer = uiState.customers.find { it.kode == uiState.searchCust }
                            val customerType = customer?.jenis_cs ?: ""
                            val updatedProducts = uiState.scannedProducts.map { product ->
                                val specialPrice = when (customerType) {
                                    "Pelanggan" -> if (product.hargaPelanggan > 0) product.hargaPelanggan else product.hargaItem
                                    "Toko" -> if (product.hargaToko > 0) product.hargaToko else product.hargaItem
                                    "Sales" -> if (product.hargaSales > 0) product.hargaSales else product.hargaItem
                                    else -> product.hargaItem
                                }
                                val originalPrice = product.hargaItem
                                val qty = product.qtyJual
                                val discount = (originalPrice - specialPrice) * qty
                                product.copy(
                                    hargaItem = originalPrice,
                                    diskon = discount
                                )
                            }
                            val scannedProductsJson =
                                Json.encodeToString(updatedProducts.map { it.toSerializable() })
                            moveToPayment(scannedProductsJson, draftId, uiState.searchCust)
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

    LaunchedEffect(entryUiState.scannedProducts, entryUiState.searchCust) {
        val customer = entryUiState.customers.find { it.kode == entryUiState.searchCust }
        val customerType = customer?.jenis_cs ?: ""
        val updatedProducts = entryUiState.scannedProducts.map { product ->
            val specialPrice = when (customerType) {
                "Pelanggan" -> if (product.hargaPelanggan > 0) product.hargaPelanggan else product.hargaItem
                "Toko" -> if (product.hargaToko > 0) product.hargaToko else product.hargaItem
                "Sales" -> if (product.hargaSales > 0) product.hargaSales else product.hargaItem
                else -> product.hargaItem
            }
            val originalPrice = product.hargaItem
            val qty = product.qtyJual
            val discount = (originalPrice - specialPrice) * qty
            product.copy(
                hargaItem = originalPrice,
                diskon = discount
            ).toSerializable()
        }
        paymentOnEvent(PaymentUiEvent.ArgumentProductsLoaded(updatedProducts))
    }

    LaunchedEffect(entryUiState.scannedProducts, entryUiState.searchCust) {
        val customer = entryUiState.customers.find { it.kode == entryUiState.searchCust }
        val customerType = customer?.jenis_cs ?: ""
        var totalHarga = 0
        var totalDiskon = 0

        entryUiState.scannedProducts.forEach { product ->
            val specialPrice = when (customerType) {
                "Pelanggan" -> if (product.hargaPelanggan > 0) product.hargaPelanggan else product.hargaItem
                "Toko" -> if (product.hargaToko > 0) product.hargaToko else product.hargaItem
                "Sales" -> if (product.hargaSales > 0) product.hargaSales else product.hargaItem
                else -> product.hargaItem
            }
            val originalPrice = product.hargaItem
            val qty = product.qtyJual
            val gross = originalPrice * qty
            val discount = (originalPrice - specialPrice) * qty

            totalHarga += gross
            totalDiskon += discount
        }
        val totalTagihan = totalHarga - totalDiskon
        entryOnEvent(EntrySalesUiEvent.OnTotalsChanged(totalHarga, totalDiskon, totalTagihan))
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

                                    product.namaBarang.contains(
                                        entryUiState.inputUser,
                                        ignoreCase = true
                                    ) -> product.namaBarang

                                    product.kodeBarang.contains(
                                        entryUiState.inputUser,
                                        ignoreCase = true
                                    ) -> product.kodeBarang

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
                            cameraLens = CameraLens.Back
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
                            onValueChange = {
                                val formatted = formatCurrencyInput(it)
                                paymentOnEvent(
                                    PaymentUiEvent.UangDiterimaChanged(
                                        formatted
                                    )
                                )
                            },
                            placehoder = "Nominal Uang",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            visualTransformation = CurrencyVisualTransformation(),
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
                                value = dev.mbakasir.com.utils.currencyFormat(paymentUiState.totalHarga.toDouble()),
                            )
                            SummaryRow(
                                label = "Diskon:",
                                value = dev.mbakasir.com.utils.currencyFormat(paymentUiState.diskon.toDouble())
                            )
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().width(1.dp)
                                    .padding(vertical = 10.dp)
                            )
                            SummaryRow(
                                label = "Totlal Tagihan",
                                value = dev.mbakasir.com.utils.currencyFormat(paymentUiState.subtotal.toDouble()),
                                isBold = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FooterButton(
                                onCancelClick = navigateBack,
                                onConfirmClick = {
                                    val uangDiterimaValue = paymentUiState.uangDiterima.replace(".", "").replace(",", "").toIntOrNull() ?: 0
                                    if (paymentUiState.uangDiterima.isEmpty() || uangDiterimaValue < paymentUiState.subtotal) {
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
package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.features.cashier_role.sales.domain.toSerializable
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.components.CustomerSection
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.components.ProductSearchSection
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.components.TransactionSummaryFooter
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
import dev.mbakasir.com.ui.component.HeadlineText
import dev.mbakasir.com.ui.component.formatCurrencyInput
import dev.mbakasir.com.ui.navigation.cashier_role.CashierScreen
import dev.mbakasir.com.ui.navigation.cashier_role.MbakKasirNavigationType
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.utils.currencyFormat
import kotlinx.serialization.json.Json
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.cancel_transaction_body
import mbakkasir.composeapp.generated.resources.cancel_transaction_title
import mbakkasir.composeapp.generated.resources.entry_sales_add
import mbakkasir.composeapp.generated.resources.entry_sales_label
import mbakkasir.composeapp.generated.resources.entry_sales_title
import mbakkasir.composeapp.generated.resources.internet_error
import mbakkasir.composeapp.generated.resources.invalid_qr
import mbakkasir.composeapp.generated.resources.items_empty_error
import mbakkasir.composeapp.generated.resources.no
import mbakkasir.composeapp.generated.resources.payment_title
import mbakkasir.composeapp.generated.resources.payment_type
import mbakkasir.composeapp.generated.resources.ups_error
import mbakkasir.composeapp.generated.resources.yes
import org.jetbrains.compose.resources.stringResource
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
            navigateBack = { navController.navigateUp() },
            draftId = draftId
        )
    } else {
        EntrySales(
            uiState = uiState,
            onEvent = { viewModel.onEvent(it) },
            moveToPayment = { scannedProducts, draftId, customerCode ->
                navController.navigate(
                    "${CashierScreen.Payment.route}/?scannedProducts=$scannedProducts&draftId=$draftId&customerCode=$customerCode"
                ) { restoreState = true }
            },
            navigateBack = { navController.navigateUp() },
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
    val itemsEmptyError = stringResource(Res.string.items_empty_error)
    val invalidQrError = stringResource(Res.string.invalid_qr)

    LaunchedEffect(draftId) {
        if (draftId != null) {
            onEvent(EntrySalesUiEvent.LoadScannedProducts(draftId))
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { state.addError(Exception(uiState.errorMessage)) }
    }

    ContentWithMessageBar(
        messageBarState = state,
        errorMaxLines = 2,
        showCopyButton = false,
        visibilityDuration = 3000L,
        errorContainerColor = Color.Yellow,
        modifier = Modifier.statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                Row(modifier = Modifier.padding(bottom = 32.dp)) {
                    HeadlineText(stringResource(Res.string.entry_sales_add))
                    Spacer(modifier = Modifier.width(8.dp))
                    HeadlineText(
                        text = stringResource(Res.string.entry_sales_title),
                        color = primaryText
                    )
                }

                ProductSearchSection(uiState, draftId, onEvent)

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
                    modifier =
                        Modifier.clipToBounds()
                            .clip(shape = RoundedCornerShape(size = 14.dp)),
                    flashlightOn = uiState.flashlightOn,
                    onCompletion = {
                        onEvent(EntrySalesUiEvent.OnInputUserChanged(it))
                        onEvent(EntrySalesUiEvent.ScanIconClick)
                        onEvent(EntrySalesUiEvent.ScanProduct(draftId.toString(), it))
                    },
                    onFailure = {
                        if (it.isEmpty()) {
                            state.addError(Exception(invalidQrError))
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
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    CustomerSection(uiState, onEvent)
                    Spacer(modifier = Modifier.height(16.dp))
                    TransactionSummaryFooter(
                        uiState = uiState,
                        onCancel = { showDialog.value = true },
                        onConfirm = {
                            if (uiState.scannedProducts.isEmpty()) {
                                state.addError(Exception(itemsEmptyError))
                                return@TransactionSummaryFooter
                            }
                            val customer =
                                uiState.customers.find { it.kode == uiState.searchCust }
                            val customerType = customer?.jenis_cs ?: ""
                            val scannedProductsJson =
                                Json.encodeToString(
                                    uiState.scannedProducts.map {
                                        it.toSerializable(customerType)
                                    }
                                )
                            moveToPayment(scannedProductsJson, draftId, uiState.searchCust)
                        }
                    )
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onEvent(EntrySalesUiEvent.DeleteProduct(draftId.toString()))
                        navigateBack()
                    }
                ) { Text(stringResource(Res.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(Res.string.no))
                }
            },
            title = {
                Text(
                    text = stringResource(Res.string.cancel_transaction_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    stringResource(Res.string.cancel_transaction_body),
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
    navigateBack: () -> Unit,
    draftId: String
) {
    val messageBarState = rememberMessageBarState()
    val invalidQrError = stringResource(Res.string.invalid_qr)
    val upsError = stringResource(Res.string.ups_error)
    val internetError = stringResource(Res.string.internet_error)

    LaunchedEffect(entryUiState.scannedProducts, entryUiState.searchCust) {
        val customer = entryUiState.customers.find { it.kode == entryUiState.searchCust }
        val customerType = customer?.jenis_cs ?: ""
        val updatedProducts =
            entryUiState.scannedProducts.map { product -> product.toSerializable(customerType) }
        paymentOnEvent(PaymentUiEvent.ArgumentProductsLoaded(updatedProducts))
    }

    LaunchedEffect(entryUiState.errorMessage) {
        entryUiState.errorMessage?.let {
            messageBarState.addError(Exception(entryUiState.errorMessage))
        }
    }

    Row(
        modifier =
            Modifier.fillMaxSize()
                .padding(16.dp) // Row to place EntrySales and Payment side by side
    ) {
        // Left Side: Entry Sales
        ContentWithMessageBar(
            messageBarState = messageBarState,
            modifier =
                Modifier.weight(1f) // Takes 50% of the screen width
                    .fillMaxHeight()
                    .padding(end = 8.dp) // Padding to separate from Payment
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                // Entry Sales Section
                Column(modifier = Modifier.weight(1f)) {
                    HeadlineText(stringResource(Res.string.entry_sales_label))
                    HeadlineText(
                        text = stringResource(Res.string.entry_sales_title),
                        color = secondaryText,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    ProductSearchSection(entryUiState, draftId, entryOnEvent)

                    LazyColumn {
                        items(entryUiState.scannedProducts) { product ->
                            val customer =
                                entryUiState.customers.find {
                                    it.kode == entryUiState.searchCust
                                }
                            val customerType = customer?.jenis_cs ?: ""
                            EntrySalesItem(
                                product = product,
                                customerType = customerType,
                                onIncreaseQty = {
                                    entryOnEvent(
                                        EntrySalesUiEvent.IncreaseProductQty(
                                            draftId.toString(),
                                            it
                                        )
                                    )
                                },
                                onDecreaseQty = {
                                    entryOnEvent(
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

                    if (entryUiState.startBarCodeScan) {
                        QrScanner(
                            modifier =
                                Modifier.clipToBounds()
                                    .clip(shape = RoundedCornerShape(size = 14.dp)),
                            flashlightOn = entryUiState.flashlightOn,
                            onCompletion = {
                                entryOnEvent(EntrySalesUiEvent.OnInputUserChanged(it))
                                entryOnEvent(EntrySalesUiEvent.ScanIconClick)
                                entryOnEvent(
                                    EntrySalesUiEvent.ScanProduct(draftId.toString(), it)
                                )
                            },
                            onFailure = {
                                if (it.isEmpty()) {
                                    messageBarState.addError(Exception(invalidQrError))
                                } else {
                                    messageBarState.addError(Exception(it))
                                }
                            },
                            openImagePicker = false,
                            imagePickerHandler = {
                                entryOnEvent(EntrySalesUiEvent.OnLaunchGallery(it))
                            },
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
                paymentMessageBarState.addError(Exception(upsError))
            }
        }

        LaunchedEffect(paymentUiState.isConnected) {
            if (!paymentUiState.isConnected) {
                paymentMessageBarState.addError(Exception(internetError))
            }
        }

        ContentWithMessageBar(
            messageBarState = paymentMessageBarState,
            modifier =
                Modifier.weight(1f) // Takes 50% of the screen width
                    .fillMaxHeight()
                    .padding(start = 8.dp) // Padding to separate from Entry Sales
        ) {
            if (paymentUiState.isLoading) {
                EnhancedLoading()
            } else {
                Column(modifier = Modifier.fillMaxHeight()) {
                    // Payment Section
                    Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                        HeadlineText(
                            text = stringResource(Res.string.payment_title),
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        Text(stringResource(Res.string.payment_type))
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
                                    IconButton(
                                        onClick = {
                                            paymentOnEvent(PaymentUiEvent.DateIconClicked)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Date"
                                        )
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
                                paymentOnEvent(PaymentUiEvent.UangDiterimaChanged(formatted))
                            },
                            placehoder = "Nominal Uang",
                            keyboardOptions =
                                KeyboardOptions(keyboardType = KeyboardType.Number),
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

                    Column(modifier = Modifier.imePadding()) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            SummaryRow(
                                label = "Total Harga:",
                                value = currencyFormat(paymentUiState.totalHarga.toDouble()),
                            )
                            SummaryRow(
                                label = "Diskon:",
                                value = currencyFormat(paymentUiState.diskon.toDouble())
                            )
                            HorizontalDivider(
                                modifier =
                                    Modifier.fillMaxWidth()
                                        .width(1.dp)
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
                                    val uangDiterimaValue =
                                        paymentUiState
                                            .uangDiterima
                                            .replace(".", "")
                                            .replace(",", "")
                                            .toIntOrNull()
                                            ?: 0
                                    if (paymentUiState.uangDiterima.isEmpty() ||
                                        uangDiterimaValue < paymentUiState.subtotal
                                    ) {
                                        paymentMessageBarState.addError(
                                            Exception(
                                                "Hei, uang diterima tidak bisa kurang dari total harga!"
                                            )
                                        )
                                        return@FooterButton
                                    }
                                    val method =
                                        if (selectedOption == "Tunai") "Cash" else "Kredit"
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

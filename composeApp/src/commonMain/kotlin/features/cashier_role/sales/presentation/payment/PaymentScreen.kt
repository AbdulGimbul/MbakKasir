package features.cashier_role.sales.presentation.payment

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import features.auth.presentation.login.EnhancedLoading
import features.cashier_role.sales.domain.ProductTransSerializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import rememberMessageBarState
import ui.component.DefaultTextField
import ui.component.DisabledTextField
import ui.component.FooterButton
import ui.component.HeadlineText
import ui.navigation.cashier_role.Screen
import ui.theme.dark
import ui.theme.icon
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary
import ui.theme.secondary_text
import ui.theme.stroke
import utils.currencyFormat

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel,
    navController: NavController,
    products: List<ProductTransSerializable>,
    draftId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Payment(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        moveToInvoice = {
            navController.navigate("${Screen.Invoice.route}?paymentData=$it")
        },
        navigateBack = {
            navController.navigateUp()
        },
        products = products,
        draftId = draftId
    )
}

@Composable
fun Payment(
    uiState: PaymentUiState,
    onEvent: (PaymentUiEvent) -> Unit,
    moveToInvoice: (String) -> Unit,
    navigateBack: () -> Unit,
    products: List<ProductTransSerializable>,
    draftId: String
) {
    val radioOptions = listOf("Tunai", "Kredit")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    val state = rememberMessageBarState()

    LaunchedEffect(products) {
        onEvent(PaymentUiEvent.ArgumentProductsLoaded(products))
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            state.addError(Exception("Ups, terjadi kesalahan!"))
        }
    }

    LaunchedEffect(uiState.paymentResponse) {
        uiState.paymentResponse?.let { response ->
            if (response.code == "200") {
                uiState.products.forEach {
                    onEvent(PaymentUiEvent.DeleteScannedProducts(draftId))
                }
                val jsonResponse = Json.encodeToString(response)
                moveToInvoice(jsonResponse)
            }
        }
    }

    LaunchedEffect(uiState.isConnected) {
        if (!uiState.isConnected) {
            state.addError(Exception("Awas, internetmu mati!"))
        }
    }

    ContentWithMessageBar(
        messageBarState = state, errorMaxLines = 2, showCopyButton = false,
        visibilityDuration = 3000L,
        modifier = Modifier.statusBarsPadding()
    ) {
        if (uiState.isLoading) {
            EnhancedLoading()
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeadlineText(
                        text = "Pembayaran",
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    Text(
                        text = "Jenis Pembayaran",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = dark,
                    )
                    PaymentOptions(
                        radioOptions = radioOptions,
                        selectedOption = selectedOption,
                        onOptionSelected
                    )
                    if (selectedOption == "Kredit") {
                        Text(
                            text = "Jatuh Tempo",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = dark,
                        )
                        OutlinedTextField(
                            value = uiState.selectedDate,
                            onValueChange = {},
                            textStyle = MaterialTheme.typography.bodyMedium,
                            label = {
                                Text(
                                    "dd/mm/yyyy",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondary_text
                                )
                            },
                            enabled = false,
                            trailingIcon = {
                                IconButton(onClick = { onEvent(PaymentUiEvent.DateIconClicked) }) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Date",
                                        tint = stroke
                                    )
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledPlaceholderColor = secondary_text,
                                disabledBorderColor = stroke,
                                disabledLabelColor = secondary_text,
                                disabledTextColor = primary_text,
                                disabledTrailingIconColor = icon,
                            ),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )
                    }
                    Text(
                        text = "Uang Diterima",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = dark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    DefaultTextField(
                        value = uiState.uangDiterima,
                        onValueChange = {
                            onEvent(PaymentUiEvent.UangDiterimaChanged(it))
                        },
                        placehoder = "Nominal Uang",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Kembalian",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = dark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    DisabledTextField(
                        value = uiState.kembalian.toString(),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                }
                Column(
                    modifier = Modifier.imePadding()
                ) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        SummaryRow(
                            label = "Total Harga:",
                            value = currencyFormat(uiState.totalHarga.toDouble()),
                        )
                        SummaryRow(
                            label = "Diskon:",
                            value = currencyFormat(uiState.diskon.toDouble())
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth().width(1.dp)
                                .padding(vertical = 10.dp)
                        )
                        SummaryRow(
                            label = "Totlal Tagihan",
                            value = currencyFormat(uiState.subtotal.toDouble()),
                            isBold = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FooterButton(
                            onCancelClick = navigateBack,
                            onConfirmClick = {
                                if (uiState.uangDiterima.isEmpty() || uiState.uangDiterima.toInt() < uiState.subtotal) {
                                    state.addError(Exception("Hei, uang diterima tidak bisa kurang dari total harga!"))
                                    return@FooterButton
                                }
                                val method =
                                    if (selectedOption == "Tunai") "Cash" else "Kredit"
                                onEvent(PaymentUiEvent.ConfirmButtonClicked(method))
                            },
                            cancelText = "Kembali",
                            confirmText = "Bayar",
                            borderCancelColor = icon,
                            contentCancelColor = icon
                        )
                    }
                }
            }
        }
    }

    if (uiState.showDatePicker) {
        WheelDatePickerView(
            modifier = Modifier.padding(top = 18.dp, bottom = 10.dp).fillMaxWidth(),
            showDatePicker = uiState.showDatePicker,
            title = "Pilih Tanggal",
            titleStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                color = dark,
            ),
            doneLabelStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primary,
            ),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = secondary,
            ),
            dateTextColor = primary,
            rowCount = 5,
            height = 170.dp,
            onDoneClick = { date ->
                val formattedDate = "${date.dayOfMonth}/${date.monthNumber}/${date.year}"
                onEvent(PaymentUiEvent.SelectedDateChanged(formattedDate))
            },
            dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
            onDismiss = {
                onEvent(PaymentUiEvent.DismissDialog)
            }
        )
    }
}

@Composable
fun PaymentOptions(
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                    ).padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondary_text,
                )
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) else MaterialTheme.typography.bodyMedium,
            color = if (isBold) dark else secondary_text
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) else MaterialTheme.typography.bodyMedium,
            color = dark
        )
    }
}
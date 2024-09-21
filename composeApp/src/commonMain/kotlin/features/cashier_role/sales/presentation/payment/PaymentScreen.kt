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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.auth.presentation.EnhancedLoading
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.ProductTransSerializable
import features.cashier_role.sales.domain.toDetailPayload
import features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen
import network.NetworkError
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import rememberMessageBarState
import ui.component.DefaultTextField
import ui.component.DisabledTextField
import ui.component.FooterButton
import ui.component.HeadlineText
import ui.theme.dark
import ui.theme.icon
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary
import ui.theme.secondary_text
import ui.theme.stroke
import util.currencyFormat


data class PaymentScreen(val products: List<ProductTransSerializable>) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PaymentViewModel>()
        val errorMessage by viewModel.errorMessage.collectAsState()
        val paymentResponse by viewModel.paymentResponse.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val storeInfo by viewModel.store.collectAsState()
        var uangDiterima by remember { mutableStateOf("") }
        val radioOptions = listOf("Tunai", "Kredit")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf("") }
        var totalHarga by remember { mutableStateOf(0) }
        var diskon by remember { mutableStateOf(0) }
        var subtotal by remember { mutableStateOf(0) }
        var kembalian by remember { mutableStateOf(0) }
        val state = rememberMessageBarState()
        val isConnented by viewModel.connectivity.value.isConnectedState.collectAsState()

        LaunchedEffect(products) {
            products.forEach { productTrans ->
                println("Products cek: ${productTrans.subtotal}")
                totalHarga = products.sumOf { it.subtotal }
                diskon = products.sumOf { it.diskon }
                subtotal = totalHarga - diskon
            }
        }

        LaunchedEffect(errorMessage) {
            if (errorMessage == NetworkError.UNAUTHORIZED) {
                state.addError(Exception("Ups, terjadi kesalahan!"))
            }
        }

        LaunchedEffect(paymentResponse) {
            paymentResponse?.let { response ->
                if (response.message == "Insert Succesful") {
                    products.forEach {
                        viewModel.deleteScannedProducts(it.id_barang)
                    }

                    navigator.popUntil { it is EntrySalesScreen }
                    navigator.replace(
                        ReceiptScreen(
                            response,
                            totalHarga,
                            subtotal,
                            storeInfo
                        )
                    )
                }
            }
        }

        LaunchedEffect(isConnented) {
            if (!isConnented) {
                state.addError(Exception("Awas, internetmu mati!"))
            }
        }

        ContentWithMessageBar(
            messageBarState = state, errorMaxLines = 2, showCopyButton = false,
            visibilityDuration = 3000L,
            modifier = Modifier.statusBarsPadding()
        ) {
            if (isLoading) {
                EnhancedLoading()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .navigationBarsPadding()
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
                        if (selectedOption == "Kredit") {
                            Text(
                                text = "Jatuh Tempo",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = dark,
                            )
                            OutlinedTextField(
                                value = selectedDate,
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
                                    IconButton(onClick = { showDatePicker = true }) {
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
                            value = uangDiterima,
                            onValueChange = {
                                uangDiterima = it
                                kembalian = (uangDiterima.toIntOrNull() ?: 0) - subtotal
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
                            value = kembalian.toString(),
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
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
                                    "Total Harga: ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondary_text
                                )
                                Text(
                                    currencyFormat(totalHarga.toDouble()),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = dark
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Diskon: ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = secondary_text
                                )
                                Text(
                                    currencyFormat(diskon.toDouble()),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = dark
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().width(1.dp)
                                    .padding(vertical = 10.dp)
                            )
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
                                    currencyFormat(subtotal.toDouble()), color = dark,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            FooterButton(
                                onCancelClick = {
                                    navigator.pop()
                                },
                                onConfirmClick = {
                                    if (uangDiterima.isEmpty() || uangDiterima.toInt() < subtotal) {
                                        state.addError(Exception("Hei, uang diterima tidak bisa kurang dari total harga!"))
                                        return@FooterButton
                                    }
                                    val method =
                                        if (selectedOption == "Tunai") "Cash" else "Kredit"
                                    viewModel.createPayment(
                                        CreatePaymentRequest(
                                            kembali = kembalian.toString(),
                                            bayar = uangDiterima,
                                            metode = method,
                                            kasir = "3",
                                            cus = "1",
                                            nominal_ppn = "0",
                                            tempo = selectedDate,
                                            detil = products.map { it.toDetailPayload() }
                                        )
                                    )
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

        if (showDatePicker) {
            WheelDatePickerView(
                modifier = Modifier.padding(top = 18.dp, bottom = 10.dp).fillMaxWidth(),
                showDatePicker = showDatePicker,
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
                    selectedDate = formattedDate
                    showDatePicker = false
                },
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                onDismiss = {
                    showDatePicker = false
                }
            )
        }
    }
}
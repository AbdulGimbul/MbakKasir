package dev.mbakasir.com.features.cashier_role.sales.presentation.invoice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import network.chaintech.composeMultiplatformScreenCapture.ScreenCaptureComposable
import network.chaintech.composeMultiplatformScreenCapture.rememberScreenCaptureController

@Composable
fun InvoiceScreen(
    viewModel: InvoiceViewModel,
    navController: NavController,
    paymentData: dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState? = null,
    noInvoice: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Invoice(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        paymentData = paymentData,
        noInvoice = noInvoice,
        navigateBack = {
            navController.navigate(dev.mbakasir.com.ui.navigation.cashier_role.Screen.Sales.route) {
                popUpTo(dev.mbakasir.com.ui.navigation.cashier_role.Screen.Sales.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun Invoice(
    uiState: InvoiceUiState,
    onEvent: (InvoiceUiEvent) -> Unit,
    paymentData: dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState? = null,
    noInvoice: String? = null,
    navigateBack: () -> Unit
) {
    val captureController = rememberScreenCaptureController()

    LaunchedEffect(paymentData, noInvoice) {
        when {
            paymentData != null -> {
                onEvent(InvoiceUiEvent.ArgumentPaymentLoaded(paymentData))
            }

            noInvoice != null -> {
                onEvent(InvoiceUiEvent.ArgumentNoInvoiceLoaded(noInvoice))
            }
        }
    }

    if (uiState.isLoading) {
        dev.mbakasir.com.features.auth.presentation.login.EnhancedLoading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .imePadding()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            ScreenCaptureComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                screenCaptureController = captureController,
                shareImage = true,
                onCaptured = { img, throwable ->

                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0XFFDBFFF6), RoundedCornerShape(10.dp))
                            .padding(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.store?.nama.toString(),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = dev.mbakasir.com.ui.theme.dark,
                                modifier = Modifier.padding(8.dp),
                            )
                            Text(
                                text = uiState.store?.alamat.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = uiState.store?.telp.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = uiState.invoiceNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark
                            )
                            Text(
                                text = uiState.tanggal,
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = uiState.method,
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark
                            )
                            Text(
                                text = uiState.kasir,
                                style = MaterialTheme.typography.bodyMedium,
                                color = dev.mbakasir.com.ui.theme.dark
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = dev.mbakasir.com.ui.theme.stroke
                    )
                    Text(
                        text = "Produk:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = dev.mbakasir.com.ui.theme.dark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    uiState.detil.forEach {
                        ItemRow(
                            name = it.namaBarang,
                            qty = it.qtyJual,
                            price = dev.mbakasir.com.utils.currencyFormat(
                                it.subtotal.toDoubleOrNull() ?: 0.0
                            ),
                            discount = dev.mbakasir.com.utils.currencyFormat(
                                it.diskon.toDoubleOrNull() ?: 0.0
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DashedDivider(color = dev.mbakasir.com.ui.theme.dark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    TotalRow(
                        label = "TOTAL HARGA:",
                        amount = dev.mbakasir.com.utils.currencyFormat(uiState.totalHarga)
                    )
                    TotalRow(
                        label = "PPN:",
                        amount = dev.mbakasir.com.utils.currencyFormat(uiState.ppn)
                    )
                    TotalRow(
                        label = "DISKON:",
                        amount = "- ${dev.mbakasir.com.utils.currencyFormat(uiState.diskon)}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TotalRow(
                        label = "TOTAL TAGIHAN:",
                        amount = dev.mbakasir.com.utils.currencyFormat(uiState.subtotal),
                        isBold = true
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = dev.mbakasir.com.ui.theme.stroke
                    )
                    TotalRow(
                        label = "TUNAI:",
                        amount = dev.mbakasir.com.utils.currencyFormat(uiState.bayar)
                    )
                    TotalRow(
                        label = "KEMBALIAN:",
                        amount = dev.mbakasir.com.utils.currencyFormat(uiState.kembali)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "==TERIMA KASIH SUDAH BERBELANJA==",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = dev.mbakasir.com.ui.theme.secondary_text,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "BARANG YANG SUDAH DIBELI TIDAK BOLEH DIKEMBALIKAN",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = dev.mbakasir.com.ui.theme.secondary_text,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column {
                HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = { captureController.capture() }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Cetak",
                                tint = dev.mbakasir.com.ui.theme.primary
                            )
                            Text(
                                "Cetak",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = dev.mbakasir.com.ui.theme.primary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    TextButton(onClick = { navigateBack() }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (noInvoice != null) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBackIosNew,
                                    contentDescription = "Kembali",
                                    tint = dev.mbakasir.com.ui.theme.icon
                                )
                                Text(
                                    "Kembali",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = dev.mbakasir.com.ui.theme.icon,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Selesai",
                                    tint = dev.mbakasir.com.ui.theme.icon
                                )
                                Text(
                                    "Selesai",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = dev.mbakasir.com.ui.theme.icon,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ItemRow(name: String, qty: String, price: String, discount: String) {
    Column {
        Row {
            Text(
                text = name,
                color = dev.mbakasir.com.ui.theme.secondary_text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = qty,
                textAlign = TextAlign.Center,
                color = dev.mbakasir.com.ui.theme.secondary_text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = price,
                textAlign = TextAlign.End,
                color = dev.mbakasir.com.ui.theme.secondary_text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(2f)
            )
        }
        Text(
            text = "Diskon: $discount",
            textAlign = TextAlign.End,
            color = dev.mbakasir.com.ui.theme.secondary_text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp)
        )
    }
}

@Composable
fun TotalRow(label: String, amount: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) else MaterialTheme.typography.bodyMedium,
            color = dev.mbakasir.com.ui.theme.dark
        )
        Text(
            text = amount,
            style = if (isBold) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) else MaterialTheme.typography.bodyMedium,
            color = dev.mbakasir.com.ui.theme.dark
        )
    }
}

@Composable
fun DashedDivider(
    color: Color = Color.Red,
    thickness: Dp = 1.dp,
    intervals: FloatArray = floatArrayOf(10f, 5f),
    phase: Float = 0f,
    startMargin: Dp = 0.dp,
    endMargin: Dp = 0.dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val pathEffect = PathEffect.dashPathEffect(intervals, phase)

    Canvas(
        modifier = modifier
    ) {
        val dividerHeight = thickness.toPx()
        val startX = startMargin.toPx()
        val endX = size.width - endMargin.toPx()

        drawLine(
            color = color,
            strokeWidth = dividerHeight,
            start = Offset(startX, 0f),
            end = Offset(endX, 0f),
            pathEffect = pathEffect,
            cap = StrokeCap.Butt
        )
    }
}
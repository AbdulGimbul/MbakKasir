package features.cashier_role.sales.presentation.payment

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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.auth.domain.Toko
import features.cashier_role.sales.domain.CreatePaymentApiModel
import network.chaintech.composeMultiplatformScreenCapture.ScreenCaptureComposable
import network.chaintech.composeMultiplatformScreenCapture.rememberScreenCaptureController
import ui.theme.dark
import ui.theme.icon
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.stroke
import util.currencyFormat


data class ReceiptScreen(
    val receipt: CreatePaymentApiModel,
    val totalHarga: Int,
    val totalTagihan: Int,
    val toko: Toko?
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val captureController = rememberScreenCaptureController()

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
                                text = toko?.nama.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = dark,
                                modifier = Modifier.padding(8.dp),
                            )
                            Text(
                                text = toko?.alamat.toString(),
                                color = dark,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(text = toko?.telp.toString(), color = dark)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = receipt.data.invoice, color = dark)
                            Text(text = receipt.data.tanggal, color = dark)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = receipt.data.method, color = dark)
                            Text(text = receipt.data.kasir, color = dark)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = stroke)
                    Text(
                        text = "Produk:",
                        style = MaterialTheme.typography.titleSmall,
                        color = dark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    receipt.data.detil.forEach {
                        ItemRow(
                            name = it.nama_barang,
                            qty = it.qty_jual,
                            price = currencyFormat(it.subtotal.toDouble()),
                            discount = currencyFormat(it.diskon.toDouble())
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DashedDivider(color = dark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    TotalRow(label = "TOTAL HARGA:", amount = currencyFormat(totalHarga.toDouble()))
                    TotalRow(label = "PPN:", amount = currencyFormat(receipt.data.ppn.toDouble()))
                    TotalRow(
                        label = "DISKON:",
                        amount = "- ${currencyFormat(receipt.data.detil.sumOf { it.diskon.toDoubleOrNull() ?: 0.0 })}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TotalRow(
                        label = "TOTAL TAGIHAN:",
                        amount = currencyFormat(totalTagihan.toDouble()),
                        isBold = true
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = stroke)
                    TotalRow(
                        label = "TUNAI:",
                        amount = currencyFormat(receipt.data.bayar.toDouble())
                    )
                    TotalRow(
                        label = "KEMBALIAN:",
                        amount = currencyFormat(receipt.data.kembali.toDouble())
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "==TERIMA KASIH SUDAH BERBELANJA==",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = primary_text,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "BARANG YANG SUDAH DIBELI TIDAK BOLEH DIKEMBALIKAN",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = primary_text,
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
                                tint = primary
                            )
                            Text(
                                "Cetak",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = primary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    TextButton(onClick = { navigator.pop() }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Selesai",
                                tint = icon
                            )
                            Text(
                                "Kembali",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = icon,
                                modifier = Modifier.padding(start = 4.dp)
                            )
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
                Text(text = name, color = primary_text, modifier = Modifier.weight(3f))
                Text(
                    text = qty,
                    textAlign = TextAlign.Center,
                    color = primary_text,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = price,
                    textAlign = TextAlign.End,
                    color = primary_text,
                    modifier = Modifier.weight(2f)
                )
            }
            Text(
                text = "Diskon: $discount",
                textAlign = TextAlign.End,
                color = primary_text,
                modifier = Modifier.fillMaxWidth().padding(end = 24.dp)
            )
        }
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
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = dark
        )
        Text(
            text = amount,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = dark
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
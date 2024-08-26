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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import ui.theme.dark
import ui.theme.icon
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.stroke


data class ReceiptScreen(
    val receipt: CreatePaymentApiModel,
    val totalHarga: Int,
    val totalTagihan: Int,
    val toko: Toko?
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize()
                .imePadding()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
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
                        price = "Rp. ${it.subtotal}",
                        discount = "Rp. ${it.diskon}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                DashedDivider(color = dark, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                TotalRow(label = "TOTAL HARGA:", amount = "Rp. $totalHarga")
                TotalRow(label = "PPN:", amount = "Rp. ${receipt.data.ppn}")
                TotalRow(
                    label = "DISKON:",
                    amount = "- Rp. ${receipt.data.detil.sumOf { it.diskon.toIntOrNull() ?: 0 }}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TotalRow(label = "TOTAL TAGIHAN:", amount = "Rp. $totalTagihan", isBold = true)
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = stroke)
                TotalRow(label = "TUNAI:", amount = "Rp. ${receipt.data.bayar}")
                TotalRow(label = "KEMBALIAN:", amount = "Rp. ${receipt.data.kembali}")
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
            Column {
                HorizontalDivider(modifier = Modifier.fillMaxWidth().width(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Cetak",
                                tint = primary
                            )
                        }
                        Text(
                            "Cetak",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = primary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Selesai",
                                tint = icon
                            )
                        }
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
package features.cashier_role.sales.presentation.entry_sales

import ContentWithMessageBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.cashier_role.sales.domain.toSerializable
import features.cashier_role.sales.presentation.payment.PaymentScreen
import qrscanner.QrScanner
import rememberMessageBarState
import ui.component.EntrySalesItem
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.red
import ui.theme.secondary_text
import ui.theme.stroke
import util.currencyFormat

class EntrySalesScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<EntrySalesViewModel>()
        val searchResults by viewModel.searchResults.collectAsState()
        val scannedProducts by viewModel.scannedProducts.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()
        val startBarCodeScan by viewModel.startBarCodeScan.collectAsState()
        var inputUser by remember { mutableStateOf("") }
        val state = rememberMessageBarState()
        val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
        val expanded = allowExpanded && searchResults.isNotEmpty()
        var flashlightOn by remember { mutableStateOf(false) }
        var launchGallery by remember { mutableStateOf(false) }
        var totalTagihan by remember { mutableStateOf(0) }

        LaunchedEffect(scannedProducts) {
            totalTagihan = scannedProducts.sumOf { it.harga_item * it.qty_jual }
        }

        LaunchedEffect(errorMessage) {
            if (errorMessage.isNotEmpty()) {
                state.addError(Exception(errorMessage))
                viewModel.resetErrorMessage()
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
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        "Entry:",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = dark
                    )
                    Text(
                        "Penjualan",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = secondary_text,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = setExpanded
                    ) {
                        OutlinedTextField(
                            value = inputUser,
                            onValueChange = { newBarcode ->
                                inputUser = newBarcode
                                if (newBarcode.length >= 5) {
                                    viewModel.searchProductsByBarcode(newBarcode)
                                }
                            },
                            label = { Text("Scan Barcode", color = secondary_text) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.onScanIconClick()
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
                                .menuAnchor(),
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
                            searchResults.forEach { product ->
                                val displayText = when {
                                    product.barcode.contains(
                                        inputUser,
                                        ignoreCase = true
                                    ) -> product.barcode

                                    product.nama_barang.contains(
                                        inputUser,
                                        ignoreCase = true
                                    ) -> product.nama_barang

                                    product.kode_barang.contains(
                                        inputUser,
                                        ignoreCase = true
                                    ) -> product.kode_barang

                                    else -> ""
                                }

                                if (displayText.isNotEmpty()) {
                                    DropdownMenuItem(
                                        onClick = {
                                            inputUser = product.barcode
                                            viewModel.scanProductByBarcode(product.barcode)
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
                        items(scannedProducts) { product ->
                            EntrySalesItem(
                                product = product,
                                onIncreaseQty = { viewModel.increaseProductQty(it) },
                                onDecreaseQty = { viewModel.decreaseProductQty(it) },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                if (startBarCodeScan) {
                    QrScanner(
                        modifier = Modifier
                            .clipToBounds()
                            .clip(shape = RoundedCornerShape(size = 14.dp)),
                        flashlightOn = flashlightOn,
                        onCompletion = {
                            inputUser = it
                            viewModel.onScanIconClick()
                            viewModel.scanProductByBarcode(it)
                        },
                        onFailure = {
                            if (it.isEmpty()) {
                                state.addError(Exception("Invalid qr code"))
                            } else {
                                state.addError(Exception(it))
                            }
                        },
                        openImagePicker = false,
                        imagePickerHandler = { launchGallery = it },
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
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                currencyFormat(totalTagihan.toDouble()), color = dark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedButton(
                                onClick = { navigator.pop() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = red),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = red
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Batal",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (scannedProducts.isEmpty()) {
                                        state.addError(Exception("EKhm, barangnya ditambahkan dulu ya!"))
                                        return@Button
                                    }
                                    navigator.push(PaymentScreen(scannedProducts.map { it.toSerializable() }))
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Pembayaran",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
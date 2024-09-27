package features.cashier_role.sales.presentation.entry_sales

import ContentWithMessageBar
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import features.cashier_role.sales.domain.toSerializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import qrscanner.QrScanner
import rememberMessageBarState
import ui.component.EntrySalesItem
import ui.component.FooterButton
import ui.component.HeadlineText
import ui.navigation.cashier_role.Screen
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary_text
import ui.theme.stroke
import utils.currencyFormat

@Composable
fun EntrySalesScreen(viewModel: EntrySalesViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EntrySales(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        moveToPayment = {
            navController.navigate("${Screen.Payment.route}/$it")
        },
        navigateBack = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrySales(
    uiState: EntrySalesUiState,
    onEvent: (EntrySalesUiEvent) -> Unit,
    moveToPayment: (String) -> Unit,
    navigateBack: () -> Unit,
) {

    val state = rememberMessageBarState()
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && uiState.searchResults.isNotEmpty()

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
                .imePadding()
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
            ) {
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
                                        onEvent(EntrySalesUiEvent.ScanProduct(product.barcode))
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
                            onIncreaseQty = { onEvent(EntrySalesUiEvent.IncreaseProductQty(it)) },
                            onDecreaseQty = { onEvent(EntrySalesUiEvent.DecreaseProductQty(it)) },
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
                        onEvent(EntrySalesUiEvent.ScanProduct(it))
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
                            navigateBack()
                        },
                        onConfirmClick = {
                            if (uiState.scannedProducts.isEmpty()) {
                                state.addError(Exception("Ekhm, barangnya ditambahkan dulu ya!"))
                                return@FooterButton
                            }
                            val scannedProductsJson =
                                Json.encodeToString(uiState.scannedProducts.map { it.toSerializable() })
                            moveToPayment(scannedProductsJson)
                        },
                        cancelText = "Batal",
                        confirmText = "Pembayaran"
                    )
                }
            }
        }
    }
}

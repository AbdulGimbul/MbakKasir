package features.cashier_role.sales.presentation.entry_sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.auth.data.AuthRepository
import features.cashier_role.home.domain.toProductTrans
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.ProductTrans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntrySalesViewModel(
    private val salesRepository: SalesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntrySalesUiState())
    val uiState: StateFlow<EntrySalesUiState> = _uiState
    private var searchJob: Job? = null

    fun onEvent(event: EntrySalesUiEvent) {
        when (event) {
            is EntrySalesUiEvent.LoadScannedProducts -> {
                loadScannedProducts(event.draftId)
            }

            is EntrySalesUiEvent.FlashLightClick -> {
                _uiState.value =
                    _uiState.value.copy(flashlightOn = !_uiState.value.flashlightOn)
            }

            is EntrySalesUiEvent.OnLaunchGallery -> {
                _uiState.value = _uiState.value.copy(launchGallery = event.launchGallery)
            }

            is EntrySalesUiEvent.OnTotalTagihanChanged -> {
                _uiState.value = _uiState.value.copy(totalTagihan = event.totalTagihan)
            }

            is EntrySalesUiEvent.OnInputUserChanged -> {
                _uiState.value = _uiState.value.copy(inputUser = event.inputUser)
            }

            is EntrySalesUiEvent.ScanProduct -> {
                scanProductByBarcode(event.draftId, event.barcode)
            }

            is EntrySalesUiEvent.SearchProduct -> {
                searchProduct()
            }

            is EntrySalesUiEvent.ScanIconClick -> {
                _uiState.value =
                    _uiState.value.copy(startBarCodeScan = !_uiState.value.startBarCodeScan)
            }

            is EntrySalesUiEvent.IncreaseProductQty -> {
                increaseProductQty(event.draftId, event.product)
            }

            is EntrySalesUiEvent.DecreaseProductQty -> {
                decreaseProductQty(event.draftId, event.product)
            }

            is EntrySalesUiEvent.DeleteProduct -> {
                if (_uiState.value.scannedProducts.isNotEmpty()) {
                    _uiState.value.scannedProducts.forEach {
                        deleteScannedProducts(event.draftId)
                    }
                }
            }
        }
    }

    private fun scanProductByBarcode(draftId: String, barcode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val cashier = authRepository.userInfo().userInfo.nama
            _uiState.value = _uiState.value.copy(errorMessage = null)
            salesRepository.getProductByBarcode(barcode).collectLatest { product ->
                product?.let { newProduct ->
                    val currentList = _uiState.value.scannedProducts
                    if (!currentList.any { it.barcode == product.barcode }) {
                        val scannedProduct = newProduct.toProductTrans()
                        salesRepository.addProductTransToDraft(
                            draftId,
                            cashier,
                            scannedProduct
                        )
                        loadScannedProducts(draftId)
                    } else {
                        _uiState.value =
                            _uiState.value.copy(errorMessage = "Ups, barang ini sudah ditambahkan ya!")
                    }
                }
            }
        }
    }

    private fun searchProduct() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            salesRepository.searchProductsByBarcode(_uiState.value.inputUser)
                .collectLatest { products ->
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(searchResults = products)
                    }
                }
        }
    }

    private fun increaseProductQty(draftId: String, product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual + 1
            salesRepository.updateProductTransInDraft(draftId, product.id_barang, newQty)
            loadScannedProducts(draftId)
        }
    }

    private fun decreaseProductQty(draftId: String, product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual - 1
            salesRepository.updateProductTransInDraft(draftId, product.id_barang, newQty)
            loadScannedProducts(draftId)
        }
    }

    private fun loadScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            salesRepository.getProductsFromDraft(draftId.toString())
                .collectLatest { scannedProductsList ->
                    _uiState.value = _uiState.value.copy(scannedProducts = scannedProductsList)
                }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteDraft(draftId.toString())
        }
    }
}

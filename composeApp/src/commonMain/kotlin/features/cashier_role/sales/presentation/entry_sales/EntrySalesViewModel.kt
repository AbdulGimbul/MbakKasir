package features.cashier_role.sales.presentation.entry_sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntrySalesUiState())
    val uiState: StateFlow<EntrySalesUiState> = _uiState
    private var searchJob: Job? = null

    init {
        loadScannedProducts()
    }

    fun onEvent(event: EntrySalesUiEvent) {
        when (event) {
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
                scanProductByBarcode(event.barcode)
            }

            is EntrySalesUiEvent.SearchProduct -> {
                searchProduct()
            }

            is EntrySalesUiEvent.ScanIconClick -> {
                _uiState.value =
                    _uiState.value.copy(startBarCodeScan = !_uiState.value.startBarCodeScan)
            }

            is EntrySalesUiEvent.IncreaseProductQty -> {
                increaseProductQty(event.product)
            }

            is EntrySalesUiEvent.DecreaseProductQty -> {
                decreaseProductQty(event.product)
            }
        }
    }

    private fun scanProductByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            salesRepository.getProductByBarcode(barcode).collectLatest { product ->
                product?.let { newProduct ->
                    val currentList = _uiState.value.scannedProducts
                    if (!currentList.any { it.barcode == product.barcode }) {
                        val scannedProduct = newProduct.toProductTrans()
                        salesRepository.addProductTrans(scannedProduct)
                        loadScannedProducts()
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

    private fun increaseProductQty(product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual + 1
            salesRepository.updateProductTrans(product, newQty)
            loadScannedProducts()
        }
    }

    private fun decreaseProductQty(product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual - 1
            salesRepository.updateProductTrans(product, newQty)
        }
    }

    private fun loadScannedProducts() {
        viewModelScope.launch(Dispatchers.Main) {
            salesRepository.getScannedProducts().collectLatest { scannedProductsList ->
                _uiState.value = _uiState.value.copy(scannedProducts = scannedProductsList)
            }
        }
    }
}
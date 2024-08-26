package features.cashier_role.sales.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.cashier_role.home.domain.Product
import features.cashier_role.sales.data.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EntrySalesViewModel(
    private val salesRepository: SalesRepository
) : ScreenModel {

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults
    private val _scannedProducts = MutableStateFlow<List<Product>>(emptyList())
    val scannedProducts: StateFlow<List<Product>> = _scannedProducts
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    private val _startBarCodeScan = MutableStateFlow(false)
    val startBarCodeScan: StateFlow<Boolean> = _startBarCodeScan
    private var searchJob: Job? = null

    fun onScanIconClick() {
        _startBarCodeScan.value = !_startBarCodeScan.value
    }

    fun scanProductByBarcode(barcode: String) {
        screenModelScope.launch(Dispatchers.Main) {
            salesRepository.getProductByBarcode(barcode).collectLatest { product ->
                product?.let { newProduct ->
                    val currentList = _scannedProducts.value.toMutableList()
                    if (!currentList.any { it.barcode == product.barcode }) {
                        currentList.add(newProduct)
                        _scannedProducts.value = currentList
                    } else {
                        _errorMessage.value = "Barang sudah ditambahkan."
                    }
                }
            }
        }
    }

    fun searchProductsByBarcode(query: String) {
        searchJob?.cancel()
        searchJob = screenModelScope.launch(Dispatchers.IO) {
            delay(300)
            salesRepository.searchProductsByBarcode(query).collectLatest { products ->
                _searchResults.value = products
            }
        }
    }
}
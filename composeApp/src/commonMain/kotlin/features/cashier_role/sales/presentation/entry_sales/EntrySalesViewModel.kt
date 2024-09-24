package features.cashier_role.sales.presentation.entry_sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.home.domain.Product
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

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults
    private val _scannedProducts = MutableStateFlow<List<ProductTrans>>(emptyList())
    val scannedProducts: StateFlow<List<ProductTrans>> = _scannedProducts
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    private val _startBarCodeScan = MutableStateFlow(false)
    val startBarCodeScan: StateFlow<Boolean> = _startBarCodeScan
    private var searchJob: Job? = null

    init {
        loadScannedProducts()
    }

    fun onScanIconClick() {
        _startBarCodeScan.value = !_startBarCodeScan.value
    }

    fun scanProductByBarcode(inputBarcode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            salesRepository.getProductByBarcode(inputBarcode).collectLatest { product ->
                product?.let { newProduct ->
                    val currentList = _scannedProducts.value
                    if (!currentList.any { it.barcode == product.barcode }) {
                        val scannedProduct = newProduct.toProductTrans()
                        salesRepository.addProductTrans(scannedProduct)
                        loadScannedProducts()
                    } else {
                        _errorMessage.value = "Ups, barang ini sudah ditambahkan ya!"
                    }
                }
            }
        }
    }

    fun searchProductsByBarcode(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            salesRepository.searchProductsByBarcode(query).collectLatest { products ->
                withContext(Dispatchers.Main) {
                    _searchResults.value = products
                }
            }
        }
    }

    fun increaseProductQty(product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual + 1
            salesRepository.updateProductTrans(product, newQty)
            loadScannedProducts()
        }
    }

    fun decreaseProductQty(product: ProductTrans) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qty_jual - 1
            salesRepository.updateProductTrans(product, newQty)
        }
    }

    private fun loadScannedProducts() {
        viewModelScope.launch(Dispatchers.Main) {
            salesRepository.getScannedProducts().collectLatest { scannedProductsList ->
                _scannedProducts.value = scannedProductsList
            }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = ""
    }
}
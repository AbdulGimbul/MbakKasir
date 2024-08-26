package features.cashier_role.sales.presentation.entry_sales

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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

class EntrySalesViewModel(
    private val salesRepository: SalesRepository
) : ScreenModel {

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults
    private val _scannedProducts = MutableStateFlow<List<ProductTrans>>(emptyList())
    val scannedProducts: StateFlow<List<ProductTrans>> = _scannedProducts
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
                        val productTrans = newProduct.toProductTrans()
                        currentList.add(productTrans)
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

    fun increaseProductQty(product: ProductTrans) {
        val currentList = _scannedProducts.value.toMutableList()
        val updatedProduct = product.copy(
            qty_jual = product.qty_jual + 1,
            subtotal = (product.qty_jual + 1) * product.harga_item
        )
        currentList[currentList.indexOf(product)] = updatedProduct
        _scannedProducts.value = currentList
    }

    fun decreaseProductQty(product: ProductTrans) {
        if (product.qty_jual > 1) {
            val currentList = _scannedProducts.value.toMutableList()
            val updatedProduct = product.copy(
                qty_jual = product.qty_jual - 1,
                subtotal = (product.qty_jual - 1) * product.harga_item
            )
            currentList[currentList.indexOf(product)] = updatedProduct
            _scannedProducts.value = currentList
        } else {
            _errorMessage.value = "Minimal quantity is 1."
        }
    }
}
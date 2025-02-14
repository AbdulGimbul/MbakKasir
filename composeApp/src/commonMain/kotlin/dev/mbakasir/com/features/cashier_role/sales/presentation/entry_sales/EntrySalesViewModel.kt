package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProductTrans
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
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
                        val scannedProduct = newProduct.toProductTrans(draftId)
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
        if (_uiState.value.inputUser.length < 5) {
            searchJob?.cancel()
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }

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

    private fun increaseProductQty(draftId: String, product: ProductTransEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qtyJual + 1
            salesRepository.updateProductTransInDraft(draftId, product.idBarang, newQty)
            loadScannedProducts(draftId)
        }
    }

    private fun decreaseProductQty(draftId: String, product: ProductTransEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQty = product.qtyJual - 1
            salesRepository.updateProductTransInDraft(draftId, product.idBarang, newQty)
            loadScannedProducts(draftId)
        }
    }

    private fun loadScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.getProductsFromDraft(draftId)
                .collectLatest { scannedProductsList ->
                    _uiState.value = _uiState.value.copy(scannedProducts = scannedProductsList)
                }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteDraft(draftId)
        }
    }
}

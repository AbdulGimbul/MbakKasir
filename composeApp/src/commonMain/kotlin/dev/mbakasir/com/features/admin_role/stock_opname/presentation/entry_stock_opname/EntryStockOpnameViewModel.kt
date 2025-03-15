package dev.mbakasir.com.features.admin_role.stock_opname.presentation.entry_stock_opname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.admin_role.stock_opname.data.StockOpnameRepository
import dev.mbakasir.com.features.admin_role.stock_opname.domain.toUpdateStockOpnameReq
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProductStockOpname
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryStockOpnameViewModel(
    private val stockOpnameRepository: StockOpnameRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EntryStockOpnameUiState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun onEvent(event: EntryStockOpnameUiEvent) {
        when (event) {
            is EntryStockOpnameUiEvent.LoadScannedProducts -> {}

            is EntryStockOpnameUiEvent.FlashLightClick -> _uiState.value =
                _uiState.value.copy(flashlightOn = !_uiState.value.flashlightOn)

            is EntryStockOpnameUiEvent.OnLaunchGallery -> _uiState.value =
                _uiState.value.copy(launchGallery = event.launchGallery)

            is EntryStockOpnameUiEvent.OnInputUserChanged -> _uiState.value =
                _uiState.value.copy(inputUser = event.inputUser)

            is EntryStockOpnameUiEvent.SearchProduct -> searchProduct()

            is EntryStockOpnameUiEvent.ScanIconClick -> _uiState.value =
                _uiState.value.copy(startBarCodeScan = !_uiState.value.startBarCodeScan)

            is EntryStockOpnameUiEvent.ScanProduct -> scanProductByBarcode(event.barcode)

            is EntryStockOpnameUiEvent.OnJumlahChanged -> _uiState.value =
                _uiState.value.copy(product = _uiState.value.product.copy(jumlah = event.jumlah))

            is EntryStockOpnameUiEvent.OnKeteranganChanged -> _uiState.value =
                _uiState.value.copy(product = _uiState.value.product.copy(keterangan = event.keterangan))

            is EntryStockOpnameUiEvent.SubmitUpdateStock -> updateStock()
        }
    }

    private fun scanProductByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            stockOpnameRepository.getProductByBarcode(barcode).collectLatest { product ->
                product?.let { newProduct ->
                    val user = authRepository.userInfo().userInfo.username
                    val updateStockOpname = newProduct.toProductStockOpname(user)
                    _uiState.value = _uiState.value.copy(product = updateStockOpname)
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
            stockOpnameRepository.searchProductsByBarcode(_uiState.value.inputUser)
                .collectLatest { products ->
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(searchResults = products)
                    }
                }
        }
    }

    private fun updateStock() {
        _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = stockOpnameRepository.updateStockOpname(
                req = _uiState.value.product.toUpdateStockOpnameReq()
            )

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        updateStockResponse = it, isLoading = false
                    )
                    if (it.code == "200") {
                        stockOpnameRepository.updateStokById(
                            id = _uiState.value.product.idBarang,
                            newStok = _uiState.value.product.jumlah
                        )
                    }
                }.onError {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = it.message, isLoading = false)
                }
            }
        }
    }
}
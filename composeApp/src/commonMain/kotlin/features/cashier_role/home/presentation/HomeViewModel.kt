package features.cashier_role.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.domain.ProductDao
import features.cashier_role.home.domain.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val isCacheAvailable = homeRepository.checkCache()

                if (!isCacheAvailable.first()) {
                    val result = homeRepository.getProducts()
                    withContext(Dispatchers.Main) {
                        result.onSuccess { data ->
                            data.barangs.forEach { barang ->
                                homeRepository.addProduct(barang.toProduct())
                            }
                        }.onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}


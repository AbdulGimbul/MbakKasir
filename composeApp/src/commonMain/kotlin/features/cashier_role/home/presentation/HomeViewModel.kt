package features.cashier_role.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.domain.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.NetworkError
import network.onError
import network.onSuccess
import storage.MongoDB

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val mongoDB: MongoDB
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val isCacheAvailable = mongoDB.isProductCacheAvailable().first()

                if (!isCacheAvailable) {
                    val result = homeRepository.getProducts()
                    withContext(Dispatchers.Main) {
                        result.onSuccess { data ->
                            data.barangs.forEach { barang ->
                                mongoDB.addProduct(barang.toProduct())
                            }
                        }.onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = NetworkError.UNKNOWN)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}


package features.cashier_role.home.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.domain.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.NetworkError
import network.onError
import network.onSuccess

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val mongoDB: MongoDB
) : ScreenModel {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        screenModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null

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
                            _errorMessage.value = error
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = NetworkError.UNKNOWN
            } finally {
                _isLoading.value = false
            }
        }
    }
}


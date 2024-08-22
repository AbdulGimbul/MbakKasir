package features.cashier_role.home.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.domain.Barang
import features.cashier_role.home.domain.toBarang
import features.cashier_role.home.domain.toProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import network.NetworkError
import network.onError
import network.onSuccess

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val mongoDB: MongoDB
) : ScreenModel {
    private val _productsResponse = MutableStateFlow<List<Barang>>(emptyList())
    val productsResponse: StateFlow<List<Barang>> = _productsResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        screenModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                if (mongoDB.isProductCacheAvailable()) {
                    _productsResponse.value =
                        mongoDB.getProducts()?.map { it.toBarang() } ?: emptyList()
                } else {
                    val result = homeRepository.getProducts()
                    result.onSuccess { data ->
                        _productsResponse.value = data.barangs
                        data.barangs.map { barang ->
                            mongoDB.addProduct(barang.toProduct())
                        }
                    }.onError {
                        _errorMessage.value = it
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
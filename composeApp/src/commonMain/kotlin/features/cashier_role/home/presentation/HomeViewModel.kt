package features.cashier_role.home.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.cashier_role.home.data.HomeRepository
import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.domain.Barang
import features.cashier_role.home.domain.toBarang
import features.cashier_role.home.domain.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
        screenModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val isCacheAvailable = mongoDB.isProductCacheAvailable().first()

                if (isCacheAvailable) {
                    // Collect the products from the Flow
                    mongoDB.getProducts().collect { products ->
                        _productsResponse.value = products.map { it.toBarang() }
                    }
                } else {
                    // Fetch products from network
                    val result = homeRepository.getProducts()
                    result.onSuccess { data ->
                        _productsResponse.value = data.barangs
                        // Cache the fetched products
                        data.barangs.forEach { barang ->
                            mongoDB.addProduct(barang.toProduct())
                        }
                    }.onError { error ->
                        _errorMessage.value = error
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


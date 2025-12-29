package dev.mbakasir.com.features.cashier_role.sales.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProduct
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentRequest
import dev.mbakasir.com.features.cashier_role.sales.domain.toDetailPayload
import dev.mbakasir.com.features.cashier_role.sales.domain.toSerializable
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SalesViewModel(
        private val salesRepository: SalesRepository,
        private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState
    private val lastUpdateMaster = MutableStateFlow("")
    private var currentDraftId: String? = null

    init {
        getDrafts()
    }

    fun onEvent(event: SalesUiEvent) {
        when (event) {
            is SalesUiEvent.SendDraftTrans -> {
                currentDraftId = event.invoiceNumber
                sendDraftTrans(event.invoiceNumber)
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val lastUpdateCache = productRepository.getLastUpdateCache()
                val getLastUpdateMaster = productRepository.getLastUpdateMaster()

                withContext(Dispatchers.Main) {
                    getLastUpdateMaster
                            .onSuccess {
                                lastUpdateMaster.value = it.lastUpdate.toString()

                                if (lastUpdateCache.isEmpty() ||
                                                lastUpdateCache == "null" ||
                                                lastUpdateCache != lastUpdateMaster.value
                                ) {
                                    productRepository.setLastUpdateCache(lastUpdateMaster.value)
                                    fetchAndCacheProducts()
                                }
                            }
                            .onError { error ->
                                _uiState.value = _uiState.value.copy(errorMessage = error.message)
                            }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun fetchAndCacheProducts() {
        val getProducts = productRepository.getProducts()
        withContext(Dispatchers.Main) {
            getProducts
                    .onSuccess { data ->
                        productRepository.deleteAllProducts()
                        data.barangs.forEach { barang ->
                            productRepository.addProduct(barang.toProduct())
                        }
                    }
                    .onError { error ->
                        _uiState.value = _uiState.value.copy(errorMessage = error.message)
                    }
        }
    }

    private fun getDrafts() {
        viewModelScope.launch {
            salesRepository.getDrafts().collectLatest { drafts ->
                _uiState.value = _uiState.value.copy(draftList = drafts)
            }
        }
    }

    private fun sendDraftTrans(invoiceNumber: String) {
        val data = _uiState.value.draftList.find { it.draft.draftId == invoiceNumber }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            // Resolve customer type for pricing
            var customerType = ""
            val customerCode = data?.draft?.customer ?: ""
            if (customerCode.isNotEmpty()) {
                val customerResult = salesRepository.getCustomers()
                customerResult
                        .onSuccess {
                            customerType =
                                    it.customers.find { c -> c.kode == customerCode }?.jenis_cs
                                            ?: ""
                        }
                        .onError {
                            // Try to find in cache/offline if request failed effectively
                            // getCustomers already handles fallback, but we rely on its result
                            // here.
                            // If cached, it returns Success. If completely failed, empty string.
                            // If getCustomers logic relies on `customerDao`, we should be good.
                        }
            }

            val serializableItems =
                    data?.items?.map { it.toSerializable(customerType) } ?: emptyList()
            val detilPayload = serializableItems.map { it.toDetailPayload() }

            val totalAmount =
                    serializableItems.sumOf { it.subtotal } // subtotal is (qty * price) - discount
            val amountPaid = data?.draft?.amountPaid ?: 0
            val realChange = amountPaid - totalAmount

            val result =
                    salesRepository.createPayment(
                            CreatePaymentRequest(
                                    kembali = realChange.toString(),
                                    bayar = amountPaid.toString(),
                                    metode = data?.draft?.paymentMethod.toString(),
                                    kasir = "3",
                                    cus = data?.draft?.customer.toString(),
                                    nominalPpn = "0",
                                    keterangan = data?.draft?.description.toString(),
                                    tempo = "",
                                    detil = detilPayload
                            )
                    )
            withContext(Dispatchers.Main) {
                result
                        .onSuccess {
                            if (it.code == "200") {
                                _uiState.value = _uiState.value.copy(paymentResponse = it)
                                currentDraftId?.let { deleteDraftId ->
                                    deleteScannedProducts(deleteDraftId)
                                    currentDraftId = null
                                }
                            }
                        }
                        .onError {
                            _uiState.value =
                                    _uiState.value.copy(
                                            errorMessage =
                                                    "Eh kirim data gagal, coba beberapa saat lagi ya!:')"
                                    )
                        }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteDraft(draftId)
            _uiState.update { currentState ->
                currentState.copy(
                        draftList = currentState.draftList.filterNot { it.draft.draftId == draftId }
                )
            }
        }
    }
}

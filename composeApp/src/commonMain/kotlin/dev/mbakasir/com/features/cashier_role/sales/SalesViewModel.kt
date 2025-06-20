package dev.mbakasir.com.features.cashier_role.sales

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
        fetchProducts()
    }

    fun onEvent(event: SalesUiEvent) {
        when (event) {
            is SalesUiEvent.SendDraftTrans -> {
                currentDraftId = event.invoiceNumber
                sendDraftTrans(event.invoiceNumber)
            }
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val lastUpdateCache = productRepository.getLastUpdateCache()
                val getLastUpdateMaster = productRepository.getLastUpdateMaster()

                withContext(Dispatchers.Main) {
                    getLastUpdateMaster.onSuccess {
                        lastUpdateMaster.value = it.lastUpdate.toString()
                    }.onError { error ->
                        _uiState.value = _uiState.value.copy(errorMessage = error.message)
                    }
                }

                if (lastUpdateCache.isEmpty() || lastUpdateCache == "null" || lastUpdateCache != lastUpdateMaster.value) {
                    productRepository.setLastUpdateCache(lastUpdateMaster.value)
                    val getProducts = productRepository.getProducts()
                    withContext(Dispatchers.Main) {
                        getProducts.onSuccess { data ->
                            data.barangs.forEach { barang ->
                                productRepository.addProduct(barang.toProduct())
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

    private fun getDrafts() {
        viewModelScope.launch {
            salesRepository.getDrafts().collectLatest { drafts ->
                _uiState.value = _uiState.value.copy(
                    draftList = drafts
                )
            }
        }
    }

    private fun sendDraftTrans(invoiceNumber: String) {
        val data = _uiState.value.draftList.find { it.draft.draftId == invoiceNumber }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(
                CreatePaymentRequest(
                    kembali = data?.change.toString(),
                    bayar = data?.draft?.amountPaid.toString(),
                    metode = data?.draft?.paymentMethod.toString(),
                    kasir = "3",
                    cus = data?.draft?.customer.toString(),
                    nominalPpn = "0",
                    tempo = data?.draft?.dueDate.toString(),
                    detil = data?.items?.map { it.toSerializable().toDetailPayload() }
                        ?: emptyList()
                )
            )
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _uiState.value = _uiState.value.copy(paymentResponse = it)
                        currentDraftId?.let { deleteDraftId ->
                            deleteScannedProducts(deleteDraftId)
                            currentDraftId = null
                        }
                    }
                }.onError {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = "Eh kirim data gagal, coba beberapa saat lagi ya!:')")
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

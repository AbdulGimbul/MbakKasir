package dev.mbakasir.com.features.admin_role.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mbakasir.com.ui.component.HomeContent

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        isLoading = uiState.isLoading,
        username = uiState.user?.userInfo?.nama ?: "",
        role = uiState.user?.userInfo?.role ?: "",
        nominalPenjualan = uiState.nominalPenjualan,
        jumlahPenjualan = uiState.jumlahPenjualan,
        jumlahPembeli = uiState.jumlahPembeli,
        onRefresh = { viewModel.refresh() }
    )
}

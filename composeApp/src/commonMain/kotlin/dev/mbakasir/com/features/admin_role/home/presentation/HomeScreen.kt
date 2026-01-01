package dev.mbakasir.com.features.admin_role.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mbakasir.com.ui.component.HomeHeaderSection
import dev.mbakasir.com.ui.component.StatCard
import dev.mbakasir.com.ui.theme.cyanLight3
import dev.mbakasir.com.ui.theme.pinkLight3
import dev.mbakasir.com.ui.theme.purpleLight3
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.img_jml_sales
import mbakkasir.composeapp.generated.resources.img_nom_sales
import mbakkasir.composeapp.generated.resources.img_users_sales

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Home(uiState = uiState, onRefresh = { viewModel.refresh() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(uiState: HomeUiState, onRefresh: () -> Unit = {}) {
        val pullToRefreshState = rememberPullToRefreshState()

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { onRefresh() }

        PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = onRefresh,
                state = pullToRefreshState,
                modifier = Modifier.fillMaxSize()
        ) {
                Column(
                        modifier =
                                Modifier.padding(16.dp)
                                        .statusBarsPadding()
                                        .verticalScroll(rememberScrollState())
                ) {
                        HomeHeaderSection(
                                username = uiState.user?.userInfo?.nama ?: "",
                                role = uiState.user?.userInfo?.role ?: ""
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        StatCard(
                                title = "Nominal Penjualan",
                                value = uiState.nominalPenjualan,
                                imageRes = Res.drawable.img_nom_sales,
                                cardColor = cyanLight3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StatCard(
                                title = "Jumlah Penjualan",
                                value = uiState.jumlahPenjualan.substringBefore(" "),
                                suffix = "items",
                                imageRes = Res.drawable.img_jml_sales,
                                cardColor = pinkLight3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StatCard(
                                title = "Jumlah Pembeli",
                                value = uiState.jumlahPembeli.substringBefore(" "),
                                suffix = "orang",
                                imageRes = Res.drawable.img_users_sales,
                                cardColor = purpleLight3
                        )
                }
        }
}

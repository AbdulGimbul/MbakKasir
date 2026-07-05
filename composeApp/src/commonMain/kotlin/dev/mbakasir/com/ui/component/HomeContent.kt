package dev.mbakasir.com.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.cyanDeep
import dev.mbakasir.com.ui.theme.cyanLight3
import dev.mbakasir.com.ui.theme.pinkDeep
import dev.mbakasir.com.ui.theme.pinkLight3
import dev.mbakasir.com.ui.theme.purpleDeep
import dev.mbakasir.com.ui.theme.purpleLight3
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.img_jml_sales
import mbakkasir.composeapp.generated.resources.img_nom_sales
import mbakkasir.composeapp.generated.resources.img_users_sales

/**
 * Shared home content composable used by both admin and cashier home screens.
 * Displays the user header, and three gradient stat cards for sales data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    isLoading: Boolean,
    username: String,
    role: String,
    nominalPenjualan: String,
    jumlahPenjualan: String,
    jumlahPembeli: String,
    onRefresh: () -> Unit = {}
) {
    val pullToRefreshState = rememberPullToRefreshState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { onRefresh() }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier =
                Modifier.padding(Spacing.lg)
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
        ) {
            HomeHeaderSection(
                username = username,
                role = role
            )
            Spacer(modifier = Modifier.height(Spacing.xl))
            StatCard(
                title = "Nominal Penjualan",
                value = nominalPenjualan,
                imageRes = Res.drawable.img_nom_sales,
                cardColor = cyanLight3,
                cardColorEnd = cyanDeep
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            StatCard(
                title = "Jumlah Penjualan",
                value = jumlahPenjualan.substringBefore(" "),
                suffix = "items",
                imageRes = Res.drawable.img_jml_sales,
                cardColor = pinkLight3,
                cardColorEnd = pinkDeep
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            StatCard(
                title = "Jumlah Pembeli",
                value = jumlahPembeli.substringBefore(" "),
                suffix = "orang",
                imageRes = Res.drawable.img_users_sales,
                cardColor = purpleLight3,
                cardColorEnd = purpleDeep
            )
        }
    }
}

package dev.mbakasir.com.features.admin_role.product.presentation

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.ProductItem
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rememberMessageBarState

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if (index == uiState.productList.lastIndex) {
                    viewModel.getTopProduct()
                }
            }
    }

    Product(uiState = uiState, listState = listState, onRefresh = { viewModel.reloadData() })
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Product(uiState: ProductUiState, listState: LazyListState, onRefresh: () -> Unit = {}) {
    var search by remember { mutableStateOf("") }
    val state = rememberMessageBarState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { state.addError(Exception(it)) }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { onRefresh() }

    ContentWithMessageBar(
        messageBarState = state,
        errorMaxLines = 2,
        showCopyButton = false,
        visibilityDuration = 3000L,
        modifier = Modifier.statusBarsPadding()
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    "Barang",
                    style =
                        MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    color = dark,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                )
                DefaultTextField(
                    value = search,
                    onValueChange = { search = it },
                    placehoder = "Search ...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Terakhir Diperbaharui",
                            color = dark,
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                            maxLines = 2
                        )
                        Text(
                            text = uiState.latestUpdate.ifEmpty { "-" },
                            color = primary,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                    Column(modifier = Modifier.weight(0.5f), horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Jumlah Barang",
                            color = dark,
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = uiState.totalProduct.toString(),
                            color = primary,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(state = listState) {
                    items(uiState.productList) { product ->
                        ProductItem(
                            product = product,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

package dev.mbakasir.com.features.cashier_role.product.presentation

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.ProductItem
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.utils.formatDateTime
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rememberMessageBarState

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if (index == uiState.productList.lastIndex) {
                    viewModel.getTopProduct()
                }
            }
    }

    Product(
        uiState = uiState,
        listState = listState
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Product(
    uiState: ProductUiState,
    listState: LazyListState
) {
    var search by remember { mutableStateOf("") }
    val state = rememberMessageBarState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            state.addError(Exception(it))
        }
    }

    ContentWithMessageBar(
        messageBarState = state, errorMaxLines = 2, showCopyButton = false,
        visibilityDuration = 3000L,
        modifier = Modifier.statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Barang",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = dark,
                )
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CloudDownload,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
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
                Column {
                    Text(
                        "Terakhir Diperbaharui", color = dark,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = formatDateTime(uiState.latestUpdate), color = primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column {
                    Text(
                        "Jumlah Barang", color = dark,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        uiState.totalProduct.toString(), color = primary,
                        style = MaterialTheme.typography.titleMedium
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
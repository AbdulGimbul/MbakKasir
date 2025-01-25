package features.cashier_role.product.presentation

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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import features.auth.presentation.login.EnhancedLoading
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rememberMessageBarState
import ui.component.DefaultTextField
import ui.component.ProductItem
import ui.theme.dark
import ui.theme.primary

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Product(
        uiState = uiState
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Product(
    uiState: ProductUiState,
) {
    var search by remember { mutableStateOf("") }
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/nodata.json").decodeToString()
        )
    }
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
        if (uiState.isLoading) {
            EnhancedLoading()
        } else {
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
                            "Diperbaharui", color = dark,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            "12 Oktober 2023", color = primary,
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
                LazyColumn {
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
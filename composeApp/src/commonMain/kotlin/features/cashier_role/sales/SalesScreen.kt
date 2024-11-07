package features.cashier_role.sales

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import features.cashier_role.sales.presentation.SalesViewModel
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.component.DefaultTextField
import ui.component.SalesItem
import ui.navigation.cashier_role.Screen
import ui.theme.dark

@Composable
fun SalesScreen(viewModel: SalesViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Sales(
        uiState = uiState,
        moveToEntrySales = {
            navController.navigate("${Screen.EntrySales.route}/$it")
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Sales(
    uiState: SalesUiState,
    moveToEntrySales: (String) -> Unit,
) {
    var search by remember { mutableStateOf("") }
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/nodata.json").decodeToString()
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Penjualan",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = dark,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        DefaultTextField(
            value = search,
            onValueChange = { search = it },
            placehoder = "Search ...",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.draftList.isNotEmpty()) {
            LazyColumn {
                items(uiState.draftList) { product ->
                    SalesItem(
                        product = product,
                        onClick = { moveToEntrySales(product.draftId) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = Compottie.IterateForever
                    ),
                    contentDescription = "Lottie animation",
                    modifier = Modifier.size(170.dp)
                )
                Text(
                    "Data not available.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

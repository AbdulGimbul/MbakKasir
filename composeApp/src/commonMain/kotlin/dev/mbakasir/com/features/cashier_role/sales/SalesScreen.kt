package dev.mbakasir.com.features.cashier_role.sales

import ContentWithMessageBar
import androidx.compose.foundation.Image
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
import androidx.navigation.NavController
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.features.auth.presentation.login.EnhancedLoading
import dev.mbakasir.com.ui.component.SalesItem
import dev.mbakasir.com.ui.navigation.cashier_role.Screen
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_notes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import rememberMessageBarState

@Composable
fun SalesScreen(viewModel: SalesViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Sales(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        moveToEntrySales = {
            navController.navigate("${Screen.EntrySales.route}/$it")
        },
        moveToHistory = {
            navController.navigate(Screen.History.route)
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Sales(
    uiState: SalesUiState,
    onEvent: (SalesUiEvent) -> Unit,
    moveToEntrySales: (String) -> Unit,
    moveToHistory: () -> Unit
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
                        "Penjualan",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = dark,
                    )
                    IconButton(
                        onClick = moveToHistory
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_notes),
                            tint = primary,
                            contentDescription = null,
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
                Spacer(modifier = Modifier.height(16.dp))
                if (uiState.draftList.isNotEmpty()) {
                    LazyColumn {
                        items(uiState.draftList) { product ->
                            SalesItem(
                                product = product,
                                onClick = {
                                    if (product.draft.isPrinted) {
                                        onEvent(SalesUiEvent.SendDraftTrans(product.draft.draftId))
                                    } else {
                                        moveToEntrySales(product.draft.draftId)
                                    }
                                },
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
    }
}

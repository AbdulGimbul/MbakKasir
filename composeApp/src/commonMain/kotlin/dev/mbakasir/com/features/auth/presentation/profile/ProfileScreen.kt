package dev.mbakasir.com.features.auth.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.features.auth.domain.Toko
import dev.mbakasir.com.features.auth.domain.User
import dev.mbakasir.com.ui.navigation.MainScreen
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.red
import dev.mbakasir.com.utils.getBrowserHelper
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLogout) {
        LaunchedEffect(Unit) {
            navController.navigate(MainScreen.Login.route) {
                popUpTo(MainScreen.Login.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Profile(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@Composable
fun Profile(
    uiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    if (uiState.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = primary)
        }
    } else {
        Column {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                            .background(Color.White),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(primary)
                    )
                    uiState.user?.userInfo?.let { UserInfoHeader(it) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                uiState.user?.storeInfo?.let { StoreInformationCard(it) }
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedButton(
                    onClick = { onEvent(ProfileUiEvent.Logout) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = red),
                    border = BorderStroke(
                        width = 1.dp,
                        color = red
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        "Logout",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Supported by ",
                        style = MaterialTheme.typography.bodyMedium.copy(primary_text),
                    )
                    Text(
                        text = "Mbakasir.com",
                        style = MaterialTheme.typography.bodyMedium.copy(color = primary_text),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            getBrowserHelper().openBrowser("https://mbakasir.com/")
                        }
                    )
                }
                Text(
                    text = uiState.version,
                    style = MaterialTheme.typography.bodyMedium,
                    color = primary_text
                )
            }
        }
    }
}

@Composable
fun UserInfoHeader(
    user: User
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 115.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(resource = Res.drawable.account),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(80.dp)
        )
        Text(
            text = user.nama,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            color = dark,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium,
            color = dark
        )
        Text(
            text = user.role,
            style = MaterialTheme.typography.bodyMedium,
            color = primary_text
        )
    }
}

@Composable
fun StoreInformationCard(
    store: Toko
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Informasi Toko",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = dark
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(label = "Nama", info = store.nama)
            InfoRow(label = "Alamat", info = store.alamat)
            InfoRow(label = "Telp", info = store.telp)
        }
    }
}

@Composable
fun InfoRow(label: String, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = primary_text
        )
        Text(
            text = info,
            style = MaterialTheme.typography.bodyMedium,
            color = dark
        )
    }
}
package features.auth.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import features.auth.domain.Toko
import features.auth.domain.User
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import org.jetbrains.compose.resources.painterResource
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Profile(uiState)
}

@Composable
fun Profile(uiState: ProfileUiState) {

    Column(
        modifier = Modifier.fillMaxSize()
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
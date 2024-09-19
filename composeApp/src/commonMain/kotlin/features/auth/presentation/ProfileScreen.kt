package features.auth.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import features.auth.domain.Toko
import features.auth.domain.User
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import org.jetbrains.compose.resources.painterResource
import ui.theme.dark
import ui.theme.primary
import ui.theme.secondary_text

class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()

        val user by viewModel.user.collectAsState()

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
                user?.let { UserInfoHeader(it.userInfo) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            user?.let { StoreInformationCard(it.storeInfo) }
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
                fontSize = 16.sp
            ),
            color = dark,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = user.username,
            color = dark
        )
        Text(
            text = user.role,
            color = secondary_text
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
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
            color = secondary_text
        )
        Text(
            text = info,
            color = dark
        )
    }
}
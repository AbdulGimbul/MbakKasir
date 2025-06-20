package dev.mbakasir.com.features.cashier_role.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
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
import dev.mbakasir.com.ui.theme.cyanLight3
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.pinkLight3
import dev.mbakasir.com.ui.theme.purpleLight3
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.ui.theme.stroke
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import mbakkasir.composeapp.generated.resources.ic_bell
import mbakkasir.composeapp.generated.resources.img_jml_sales
import mbakkasir.composeapp.generated.resources.img_nom_sales
import mbakkasir.composeapp.generated.resources.img_users_sales
import mbakkasir.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Home(
        uiState = uiState
    )

}

@Composable
fun Home(
    uiState: HomeUiState
) {

    Column(modifier = Modifier.padding(16.dp).statusBarsPadding()) {
        HeaderSection(
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

@Composable
fun HeaderSection(
    username: String,
    role: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(resource = Res.drawable.account),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                username,
                style = MaterialTheme.typography.bodyMedium,
                color = dark
            )
            Text(
                role,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = dark
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { },
            modifier = Modifier.border(
                width = 1.dp,
                color = stroke,
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_bell),
                contentDescription = null
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    suffix: String? = null,
    imageRes: DrawableResource,
    cardColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodySmall,
                    color = secondary_text
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        value,
                        color = dark,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    if (suffix != null) {
                        Text(
                            "items",
                            color = dark,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
            Image(
                painterResource(resource = imageRes),
                contentDescription = null,
                modifier = Modifier.padding(end = 24.dp).size(68.dp)
            )
        }
    }
}
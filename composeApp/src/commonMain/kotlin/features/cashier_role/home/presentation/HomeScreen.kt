package features.cashier_role.home.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_bell
import mbakkasir.composeapp.generated.resources.img_jml_sales
import mbakkasir.composeapp.generated.resources.img_nom_sales
import mbakkasir.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.painterResource
import ui.theme.cyanLight3
import ui.theme.dark
import ui.theme.pinkLight3
import ui.theme.secondary_text
import ui.theme.stroke


class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()

        Column(modifier = Modifier.padding(16.dp).statusBarsPadding()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .border(width = 1.dp, color = stroke, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Role", style = MaterialTheme.typography.bodyMedium, color = dark)
                    Text("Penjualan", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = dark)
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
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cyanLight3
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
                        Text("Nominal penjualan", style = MaterialTheme.typography.bodySmall, color = secondary_text)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "Rp. 60.000.0000",
                                color = dark,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                    Image(
                        painterResource(resource = Res.drawable.img_nom_sales),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 24.dp).size(68.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = pinkLight3
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
                        Text("Jumlah penjualan", style = MaterialTheme.typography.bodySmall, color = secondary_text)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "1.000",
                                color = dark,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                "items",
                                color = dark,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                    Image(
                        painterResource(resource = Res.drawable.img_jml_sales),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 24.dp).size(68.dp)
                    )
                }
            }
        }
    }
}
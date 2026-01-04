package dev.mbakasir.com.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.stroke
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import mbakkasir.composeapp.generated.resources.ic_bell
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeHeaderSection(username: String, role: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(resource = Res.drawable.account),
            contentDescription = null,
            modifier = Modifier.weight(0.3f, fill = false)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(username, style = MaterialTheme.typography.bodyMedium, color = dark)
            Text(
                role,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                color = dark
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {},
            modifier =
                Modifier.border(
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

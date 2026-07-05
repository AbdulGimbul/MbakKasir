package dev.mbakasir.com.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.stroke
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.account
import mbakkasir.composeapp.generated.resources.ic_bell
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Composable
fun HomeHeaderSection(username: String, role: String) {
    val currentHour = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).hour

    val greeting = when {
        currentHour in 5..10 -> "Selamat pagi ☀\uFE0F"
        currentHour in 11..14 -> "Selamat siang \uD83C\uDF24\uFE0F"
        currentHour in 15..17 -> "Selamat sore \uD83C\uDF05"
        else -> "Selamat malam \uD83C\uDF19"
    }

    Column {
        Text(
            text = greeting,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = primary,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(Spacing.lg))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(resource = Res.drawable.account),
                contentDescription = "User avatar",
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(CornerRadius.lg))
            )
            Spacer(modifier = Modifier.width(Spacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = username,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                    color = primaryText,
                    maxLines = 1
                )
                Text(
                    text = role,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    color = dark,
                    maxLines = 1
                )
            }
            // Notification bell — visually disabled until notification feature is implemented
            IconButton(
                onClick = {},
                enabled = false,
                modifier =
                    Modifier
                        .alpha(0.4f)
                        .border(
                            width = 1.dp,
                            color = stroke,
                            shape = RoundedCornerShape(CornerRadius.sm)
                        )
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.ic_bell),
                    contentDescription = "Notifikasi (segera hadir)"
                )
            }
        }
    }
}

package dev.mbakasir.com.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.secondaryText
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Reusable empty state composable that shows a Lottie animation and a message.
 * Use this in list screens when there's no data to display.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun EmptyStateView(
    message: String,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/nodata.json").decodeToString())
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(top = Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter =
                rememberLottiePainter(
                    composition = composition,
                    iterations = Compottie.IterateForever
                ),
            contentDescription = "Tidak ada data",
            modifier = Modifier.size(170.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryText,
            modifier = Modifier.padding(top = Spacing.sm)
        )
    }
}

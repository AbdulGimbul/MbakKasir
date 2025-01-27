package dev.mbakasir.com.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.red

@Composable
fun FooterButton(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    cancelText: String,
    confirmText: String,
    borderCancelColor: Color = red,
    contentCancelColor: Color = red,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = onCancelClick,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = contentCancelColor),
            border = BorderStroke(
                width = 1.dp,
                color = borderCancelColor
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                cancelText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onConfirmClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                confirmText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
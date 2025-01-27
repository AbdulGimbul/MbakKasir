package dev.mbakasir.com.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisabledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = dev.mbakasir.com.ui.theme.disabled,
            disabledContainerColor = dev.mbakasir.com.ui.theme.disabled,
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

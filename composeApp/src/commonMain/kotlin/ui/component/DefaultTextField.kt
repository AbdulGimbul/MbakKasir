package ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.secondary_text
import ui.theme.stroke

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    minLines: Int = 1,
    singleLine: Boolean = true,
    placehoder: String? = null,
    keyboardOptions: KeyboardOptions? = null
) {
    var passwordVisibility by remember { mutableStateOf(isPassword) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        label = if (label != null) {
            {
                Text(text = label, color = secondary_text)
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = stroke,
            unfocusedBorderColor = stroke,
            cursorColor = primary_text,
            focusedLabelColor = primary,
            unfocusedLabelColor = secondary_text,
            focusedPlaceholderColor = secondary_text,
            unfocusedPlaceholderColor = secondary_text,
            focusedLeadingIconColor = secondary_text,
            unfocusedLeadingIconColor = secondary_text,
            focusedTrailingIconColor = secondary_text,
            unfocusedTrailingIconColor = secondary_text,
        ),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Username"
                )
            }
        } else null,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (isPassword && passwordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = "Password"
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        minLines = minLines,
        singleLine = singleLine,
        placeholder = if (placehoder != null) {
            {
                Text(text = placehoder, color = secondary_text)
            }
        } else null,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
    )
}
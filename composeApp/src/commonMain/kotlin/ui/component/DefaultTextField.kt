package ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
    placehoder: String? = null,
) {
    var passwordVisibility by remember { mutableStateOf(isPassword) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = if (label != null) {
            {
                Text(text = label, color = secondary_text)
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = stroke,
            unfocusedBorderColor = stroke,
            cursorColor = primary_text,
            focusedLabelColor = primary,
            unfocusedLabelColor = secondary_text,
            placeholderColor = secondary_text,
            disabledPlaceholderColor = secondary_text,
            leadingIconColor = secondary_text,
            trailingIconColor = secondary_text,
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
        placeholder = if (placehoder != null) {
            {
                Text(text = placehoder, color = secondary_text)
            }
        } else null
    )
}
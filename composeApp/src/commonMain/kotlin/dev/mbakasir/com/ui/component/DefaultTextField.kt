package dev.mbakasir.com.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.em
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.ui.theme.primaryContainer
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.stroke
import dev.mbakasir.com.ui.theme.surfaceVariant

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
        keyboardOptions: KeyboardOptions? = null,
        visualTransformation: VisualTransformation? = null,
        isError: Boolean = false,
        errorMessage: String? = null
) {
    var passwordVisibility by remember { mutableStateOf(isPassword) }
    var isFocused by remember { mutableStateOf(false) }

    // Animated border color based on focus state
    val borderColor by
            animateColorAsState(
                    targetValue =
                            when {
                                isError -> MaterialTheme.colorScheme.error
                                isFocused -> primary
                                else -> stroke
                            },
                    animationSpec = tween(durationMillis = 200),
                    label = "borderColor"
            )

    // Animated container color based on focus state
    val containerColor by
            animateColorAsState(
                    targetValue =
                            if (isFocused) primaryContainer.copy(alpha = 0.1f)
                            else surfaceVariant.copy(alpha = 0.3f),
                    animationSpec = tween(durationMillis = 200),
                    label = "containerColor"
            )

    // Animated icon color based on focus state
    val iconColor by
            animateColorAsState(
                    targetValue = if (isFocused) primary else secondaryText,
                    animationSpec = tween(durationMillis = 200),
                    label = "iconColor"
            )

    OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 1.2.em),
            label =
                    if (label != null) {
                        { Text(text = label, color = if (isFocused) primary else secondaryText) }
                    } else null,
            modifier =
                    modifier.fillMaxWidth().onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
            shape = RoundedCornerShape(CornerRadius.md),
            colors =
                    OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = stroke,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            cursorColor = primary,
                            focusedLabelColor = primary,
                            unfocusedLabelColor = secondaryText,
                            focusedPlaceholderColor = secondaryText,
                            unfocusedPlaceholderColor = secondaryText,
                            focusedLeadingIconColor = primary,
                            unfocusedLeadingIconColor = secondaryText,
                            focusedTrailingIconColor = secondaryText,
                            unfocusedTrailingIconColor = secondaryText,
                            focusedContainerColor = primaryContainer.copy(alpha = 0.05f),
                            unfocusedContainerColor =
                                    androidx.compose.ui.graphics.Color.Transparent,
                    ),
            leadingIcon =
                    if (leadingIcon != null) {
                        {
                            Icon(
                                    imageVector = leadingIcon,
                                    contentDescription = label ?: "Input icon",
                                    tint = iconColor
                            )
                        }
                    } else null,
            trailingIcon =
                    if (isPassword) {
                        {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                        imageVector =
                                                if (isPassword && passwordVisibility)
                                                        Icons.Outlined.VisibilityOff
                                                else Icons.Outlined.Visibility,
                                        contentDescription =
                                                if (passwordVisibility) "Hide password"
                                                else "Show password"
                                )
                            }
                        }
                    } else null,
            visualTransformation =
                    when {
                        isPassword && passwordVisibility -> PasswordVisualTransformation()
                        visualTransformation != null -> visualTransformation
                        else -> VisualTransformation.None
                    },
            minLines = minLines,
            singleLine = singleLine,
            isError = isError,
            supportingText =
                    if (isError && errorMessage != null) {
                        {
                            Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                        }
                    } else null,
            placeholder =
                    if (placehoder != null) {
                        { Text(text = placehoder, color = secondaryText) }
                    } else null,
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
    )
}

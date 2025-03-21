package dev.mbakasir.com.features.auth.presentation.login

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.ui.component.DefaultButton
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.EnhancedLoading
import dev.mbakasir.com.ui.navigation.MainScreen
import dev.mbakasir.com.ui.navigation.admin_role.AdminScreen
import dev.mbakasir.com.ui.navigation.cashier_role.CashierScreen
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import rememberMessageBarState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is LoginUiState.Authenticated -> {
            if (state.role == "Kasir") {
                LaunchedEffect(Unit) {
                    navController.navigate(MainScreen.Cashier.route) {
                        popUpTo(MainScreen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(MainScreen.Admin.route) {
                        popUpTo(MainScreen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        is LoginUiState.NotAuthenticated -> {
            Login(
                uiState = state,
                onEvent = { viewModel.onEvent(it) }
            )
        }
    }
}

@Composable
fun Login(
    uiState: LoginUiState.NotAuthenticated,
    onEvent: (LoginUiEvent) -> Unit

) {
    val state = rememberMessageBarState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            if (it == "Invalid username or password.")
                state.addError(Exception("Ups, coba lagi! Username atau password-nya kurang tepat."))
        }
    }

    ContentWithMessageBar(
        messageBarState = state, errorMaxLines = 2, showCopyButton = false,
        visibilityDuration = 4000L,
        modifier = Modifier.statusBarsPadding()
    ) {
        if (uiState.isLoading) {
            EnhancedLoading()
        } else {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "Welcome",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = dark,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                            color = dark
                        )
                        Text(
                            text = "\uD83D\uDC4B\uD83C\uDFFC",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        "Silahkan login ke akun yang sudah ada.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 24.dp),
                        color = secondary_text
                    )
                    DefaultTextField(
                        value = uiState.username,
                        onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
                        modifier = Modifier.padding(vertical = 8.dp),
                        leadingIcon = Icons.Outlined.AccountCircle,
                        label = "Username"
                    )
                    DefaultTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                        modifier = Modifier.padding(vertical = 8.dp),
                        leadingIcon = Icons.Outlined.Lock,
                        label = "Password",
                        isPassword = true
                    )
                    DefaultButton(
                        text = "Login",
                        onClick = {
                            if (uiState.username.isBlank() || uiState.password.isBlank()) {
                                state.addError(Exception("Username dan passwordnya diisi dulu yaa!"))
                                return@DefaultButton
                            }
                            onEvent.invoke(LoginUiEvent.Login)
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Support by Mbakasir.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = primary_text
                    )
                    Text(
                        text = "POS Ritel Version 0.0.1",
                        style = MaterialTheme.typography.bodyMedium,
                        color = primary_text
                    )
                }
            }
        }
    }
}
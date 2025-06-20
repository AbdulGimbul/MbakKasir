package dev.mbakasir.com.features.auth.presentation.login

import ContentWithMessageBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import dev.mbakasir.com.ui.theme.dark
import dev.mbakasir.com.ui.theme.primary_text
import dev.mbakasir.com.ui.theme.secondary_text
import dev.mbakasir.com.utils.getBrowserHelper
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.mbakasir_logo
import org.jetbrains.compose.resources.painterResource
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
            when(it){
                "Invalid username or password." -> state.addError(Exception("Ups, coba lagi! Username atau password-nya kurang tepat."))
                "Access denied: Token has expired" -> ""
                "Access denied: No token provided" -> ""
                else -> state.addError(Exception(it))
            }
        }
    }

    ContentWithMessageBar(
        messageBarState = state, errorMaxLines = 2, showCopyButton = false,
        visibilityDuration = 4000L,
        modifier = Modifier.statusBarsPadding().navigationBarsPadding()
    ) {
        if (uiState.isLoading) {
            EnhancedLoading()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(resource = Res.drawable.mbakasir_logo), contentDescription = null, modifier = Modifier.size(160.dp))
                    Text(
                        "Sederhana, Untung Maksimal",
                        style = MaterialTheme.typography.titleMedium,
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
                    if (uiState.isConnected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Supported by ",
                                style = MaterialTheme.typography.bodyMedium.copy(primary_text),
                            )
                            Text(
                                text = "Mbakasir.com",
                                style = MaterialTheme.typography.bodyMedium.copy(color = primary_text),
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    getBrowserHelper().openBrowser("https://mbakasir.com/")
                                }
                            )
                        }
                        Text(
                            text = uiState.version,
                            style = MaterialTheme.typography.bodyMedium,
                            color = primary_text
                        )
                    } else {
                        Text(
                            text = "Offline, tidak terhubung ke server.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = primary_text
                        )
                    }
                }
            }
        }
    }
}
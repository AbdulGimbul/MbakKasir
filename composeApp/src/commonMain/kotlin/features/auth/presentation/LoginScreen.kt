package features.auth.presentation

import ContentWithMessageBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.cashier_role.CashierScreen
import network.NetworkError
import rememberMessageBarState
import ui.component.DefaultButton
import ui.component.DefaultTextField
import ui.theme.dark
import ui.theme.primary
import ui.theme.secondary_text

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<LoginViewModel>()

        val navigator = LocalNavigator.currentOrThrow
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val isLoading by viewModel.isLoading.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()
        val loginSuccess by viewModel.loginSuccess.collectAsState()
        val state = rememberMessageBarState()

        LaunchedEffect(loginSuccess) {
            if (loginSuccess) {
                navigator.replace(CashierScreen())
            }
        }

        LaunchedEffect(errorMessage) {
            if (errorMessage == NetworkError.UNAUTHORIZED) {
                state.addError(Exception("Ups, coba lagi! Username atau password-nya kurang tepat."))
            }
        }

        if (!loginSuccess) {
            ContentWithMessageBar(
                messageBarState = state, errorMaxLines = 2, showCopyButton = false,
                visibilityDuration = 4000L,
                modifier = Modifier.statusBarsPadding()
            ) {
                if (isLoading) {
                    EnhancedLoading()
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
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
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier.padding(vertical = 8.dp),
                            leadingIcon = Icons.Outlined.AccountCircle,
                            label = "Username"
                        )
                        DefaultTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.padding(vertical = 8.dp),
                            leadingIcon = Icons.Outlined.Lock,
                            label = "Password",
                            isPassword = true
                        )
                        DefaultButton(
                            text = "Login",
                            onClick = {
                                viewModel.login(username, password)
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = primary)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Mohon tunggu...")
        }
    }
}
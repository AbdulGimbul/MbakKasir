package dev.mbakasir.com.features.auth.presentation.login

import ContentWithMessageBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.mbakasir.com.ui.component.DefaultButton
import dev.mbakasir.com.ui.component.DefaultTextField
import dev.mbakasir.com.ui.component.EnhancedLoading
import dev.mbakasir.com.ui.navigation.MainScreen
import dev.mbakasir.com.ui.theme.CornerRadius
import dev.mbakasir.com.ui.theme.Spacing
import dev.mbakasir.com.ui.theme.primaryContainer
import dev.mbakasir.com.ui.theme.primaryText
import dev.mbakasir.com.ui.theme.secondaryText
import dev.mbakasir.com.ui.theme.surface
import dev.mbakasir.com.ui.theme.surfaceVariant
import dev.mbakasir.com.utils.getBrowserHelper
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.mbakasir_logo
import org.jetbrains.compose.resources.painterResource
import rememberMessageBarState

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is LoginUiState.Authenticated -> {
            if (state.role == "Kasir" || state.role.equals("Guest", ignoreCase = true)) {
                LaunchedEffect(Unit) {
                    navController.navigate("${MainScreen.Cashier.route}/${state.role}") {
                        popUpTo(MainScreen.Login.route) { inclusive = true }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(MainScreen.Admin.route) {
                        popUpTo(MainScreen.Login.route) { inclusive = true }
                    }
                }
            }
        }

        is LoginUiState.NotAuthenticated -> {
            Login(uiState = state, onEvent = { viewModel.onEvent(it) })
        }
    }
}

@Composable
fun Login(uiState: LoginUiState.NotAuthenticated, onEvent: (LoginUiEvent) -> Unit) {

    val state = rememberMessageBarState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            when (it) {
                "Access denied: Token has expired" -> ""
                "Access denied: No token provided" -> ""
                else -> state.addError(Exception(it))
            }
        }
    }

    // Subtle gradient background
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            primaryContainer.copy(alpha = 0.3f),
            surfaceVariant.copy(alpha = 0.5f),
            surface
        )
    )

    ContentWithMessageBar(
        messageBarState = state,
        errorMaxLines = 2,
        showCopyButton = false,
        visibilityDuration = 4000L,
        modifier = Modifier.statusBarsPadding().navigationBarsPadding().imePadding()
    ) {
        if (uiState.isLoading) {
            EnhancedLoading()
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(backgroundGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Spacing.lg)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(resource = Res.drawable.mbakasir_logo),
                            contentDescription = "MbakKasir Logo",
                            modifier = Modifier.fillMaxWidth(0.4f)
                        )
                        Text(
                            "Sederhana, Untung Maksimal",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = Spacing.xl),
                            color = secondaryText,
                            maxLines = 2
                        )

                        // Floating card form
                        Card(
                            shape = RoundedCornerShape(CornerRadius.xl),
                            colors = CardDefaults.cardColors(
                                containerColor = surface.copy(alpha = 0.95f)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = dev.mbakasir.com.ui.theme.Elevation.md
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(Spacing.xl),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Silahkan Login",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    ),
                                    color = dev.mbakasir.com.ui.theme.dark,
                                    modifier = Modifier.padding(bottom = Spacing.lg)
                                )
                                DefaultTextField(
                                    value = uiState.username,
                                    onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
                                    modifier = Modifier.padding(vertical = Spacing.sm),
                                    leadingIcon = Icons.Outlined.AccountCircle,
                                    label = "Username"
                                )
                                DefaultTextField(
                                    value = uiState.password,
                                    onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                                    modifier = Modifier.padding(vertical = Spacing.sm),
                                    leadingIcon = Icons.Outlined.Lock,
                                    label = "Password",
                                    isPassword = true
                                )
                                DefaultButton(
                                    text = "Login",
                                    onClick = {
                                        if (uiState.username.isBlank() || uiState.password.isBlank()) {
                                            state.addError(
                                                Exception("Username dan passwordnya diisi dulu yaa!")
                                            )
                                            return@DefaultButton
                                        }
                                        onEvent.invoke(LoginUiEvent.Login)
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.xl)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.xl),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (uiState.isConnected) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Supported by ",
                                    style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                            color = primaryText
                                        ),
                                )
                                Text(
                                    text = "Mbakasir.com",
                                    style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                            color = primaryText
                                        ),
                                    modifier =
                                        Modifier
                                            .semantics { role = Role.Button }
                                            .clickable(
                                                interactionSource =
                                                    remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                getBrowserHelper()
                                                    .openBrowser("https://mbakasir.com/")
                                            }
                                )
                            }
                            Text(
                                text = uiState.version,
                                style = MaterialTheme.typography.bodyMedium,
                                color = primaryText
                            )
                        } else {
                            Text(
                                text = "Offline, tidak terhubung ke server.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = primaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

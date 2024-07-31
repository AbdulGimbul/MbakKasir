package features.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ui.component.DefaultButton
import ui.component.DefaultTextField
import ui.theme.dark
import ui.theme.secondary_text

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp).statusBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.h3,
                color = dark,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    color = dark
                )
                Text(text = "\uD83D\uDC4B\uD83C\uDFFC", style = MaterialTheme.typography.h4)
            }
            Text(
                "Silahkan login ke akun yang sudah ada.",
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
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            )
        }
    }
}
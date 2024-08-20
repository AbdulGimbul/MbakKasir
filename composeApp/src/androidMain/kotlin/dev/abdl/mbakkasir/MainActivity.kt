package dev.abdl.mbakkasir

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.LoginViewModel
import io.ktor.client.engine.okhttp.OkHttp
import network.createHttpClient
import storage.SessionHandler
import storage.createDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionHandler = SessionHandler(dataStore = createDataStore(applicationContext))
        val authRepository = AuthRepositoryImpl(
            httpClient = createHttpClient(
                sessionHandler = sessionHandler,
                engine = OkHttp.create()
            )
        )
        val viewModel = LoginViewModel(sessionHandler, authRepository)

        setContent {
            App(viewModel)
        }
    }
}
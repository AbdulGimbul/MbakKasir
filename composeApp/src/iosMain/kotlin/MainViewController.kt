import androidx.compose.ui.window.ComposeUIViewController
import features.auth.data.AuthRepositoryImpl
import io.ktor.client.engine.darwin.Darwin
import network.createHttpClient
import storage.SessionHandler

fun MainViewController() = ComposeUIViewController {
    val sessionHandler = SessionHandler(dataStore = createDataStore())
    val authRepository = AuthRepositoryImpl(
        sessionHandler = sessionHandler,
        httpClient = createHttpClient(Darwin.create())
    )
    App(
        sessionHandler = sessionHandler,
        authRepository = authRepository
    )
}
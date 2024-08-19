import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import features.auth.data.AuthRepositoryImpl
import io.ktor.client.engine.darwin.Darwin
import network.createHttpClient
import storage.SessionHandler
import storage.createDataStore

fun MainViewController() = ComposeUIViewController(
//    configure = {
//        initKoin()
//    }
) {
    val sessionHandler = SessionHandler(dataStore = createDataStore())
    val authRepository = AuthRepositoryImpl(
        httpClient = createHttpClient(sessionHandler = sessionHandler, engine = Darwin.create())
    )
    App(
        sessionHandler = sessionHandler,
        authRepository = authRepository
    )
}
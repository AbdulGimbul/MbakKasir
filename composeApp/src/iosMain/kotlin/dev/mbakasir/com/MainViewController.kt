package dev.mbakasir.com

import androidx.compose.ui.window.ComposeUIViewController
import dev.mbakasir.com.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
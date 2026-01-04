package dev.mbakasir.com.utils

import androidx.compose.ui.graphics.ImageBitmap

expect class ShareManager {
    suspend fun shareImage(imageBitmap: ImageBitmap, fileName: String = "invoice.png")
}

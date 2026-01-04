package dev.mbakasir.com.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.EncodedImageFormat
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareManager {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun shareImage(imageBitmap: ImageBitmap, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                val skiaBitmap = imageBitmap.asSkiaBitmap()
                val image = org.jetbrains.skia.Image.makeFromBitmap(skiaBitmap)
                val byteArray =
                    image.encodeToData(EncodedImageFormat.PNG)?.bytes ?: return@withContext

                val tempDir = NSTemporaryDirectory()
                val fileURL = NSURL.fileURLWithPath("$tempDir$fileName")

                val nsData = NSData.create(
                    bytes = byteArray.refTo(0),
                    length = byteArray.size.toULong()
                )

                nsData.writeToURL(fileURL, atomically = true)

                withContext(Dispatchers.Main) {
                    val activityViewController = UIActivityViewController(
                        activityItems = listOf(fileURL),
                        applicationActivities = null
                    )

                    val rootViewController =
                        UIApplication.sharedApplication.keyWindow?.rootViewController
                    rootViewController?.presentViewController(
                        activityViewController,
                        animated = true,
                        completion = null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

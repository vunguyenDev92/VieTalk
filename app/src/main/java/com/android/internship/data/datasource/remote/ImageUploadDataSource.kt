package com.android.internship.data.datasource.remote

import android.content.Context
import androidx.core.net.toUri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull

class ImageUploadDataSource(private val context: Context) {

    companion object {
        private const val UPLOAD_TIMEOUT_MS = 30000L
    }

    suspend fun uploadAvatar(imageUri: String, publicId: String): String {
        val resultUrl = withTimeoutOrNull(UPLOAD_TIMEOUT_MS) {
            suspendCancellableCoroutine<String> { continuation ->
                val uri = imageUri.toUri()

                try {
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

                    if (bytes == null) {
                        if (continuation.isActive) continuation.resumeWithException(Exception("Could not read bytes from URI: $imageUri"))
                        return@suspendCancellableCoroutine
                    }

                    val requestId = MediaManager.get().upload(bytes)
                        .option("public_id", publicId)
                        .option("overwrite", true)
                        .option("resource_type", "image")
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String) {}
                            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                                if (continuation.isActive) {
                                    val secureUrl = resultData["secure_url"] as? String
                                    if (secureUrl != null) {
                                        continuation.resume(secureUrl)
                                    } else {
                                        continuation.resumeWithException(Exception("Upload successful but no secure_url found."))
                                    }
                                }
                            }

                            override fun onError(requestId: String, error: ErrorInfo) {
                                if (continuation.isActive) {
                                    val errorMessage = if (error.code == ErrorInfo.NETWORK_ERROR) {
                                        "Network error. Please check your connection."
                                    } else {
                                        "Cloudinary upload failed: ${error.description}"
                                    }
                                    continuation.resumeWithException(Exception(errorMessage))
                                }
                            }

                            override fun onReschedule(requestId: String, error: ErrorInfo) {
                                if (continuation.isActive) {
                                    continuation.resumeWithException(Exception("Upload rescheduled due to a connection issue. Please try again."))
                                }
                            }
                        }).dispatch()

                    continuation.invokeOnCancellation {
                        MediaManager.get().cancelRequest(requestId)
                    }
                } catch (e: Exception) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                }
            }
        }

        return resultUrl ?: throw Exception("Upload timed out. Please try again.")
    }
}

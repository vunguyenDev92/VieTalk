package com.android.internship.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.android.internship.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.let {
            val title = remoteMessage.notification?.title
            val message = remoteMessage.notification?.body
            val imageUri = remoteMessage.notification?.imageUrl
            val data = remoteMessage.data

            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = loadBitmapFromUri(this@MyFirebaseMessagingService, imageUri)
                NotificationHelper(this@MyFirebaseMessagingService).showNotification(
                    title = title,
                    message = message,
                    bitmap = bitmap,
                    data = data,
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        val inputStream = uri?.let {
            URL(it.toString()).openStream()
        }
        if (inputStream == null) {
            return BitmapFactory.decodeResource(context.resources, R.drawable.ic_person_color)
        }
        return inputStream.use { BitmapFactory.decodeStream(it) }
    }
}

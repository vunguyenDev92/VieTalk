package com.android.internship.presentation.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.let {
            val title = remoteMessage.notification?.title
            val message = remoteMessage.notification?.body
            val imageUri = remoteMessage.notification?.imageUrl
            val data = remoteMessage.data

            NotificationHelper(this).showNotification(
                title = title,
                message = message,
                imageUri = imageUri,
                data = data,
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

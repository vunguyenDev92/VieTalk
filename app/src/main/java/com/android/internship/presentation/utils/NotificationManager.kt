package com.android.internship.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.android.internship.R
import com.android.internship.presentation.MainActivity
import java.net.URL

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "MyNotificationChannel"
        private const val CHANNEL_NAME = "My Notifications"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title: String?, message: String?, imageUri: Uri?, data: Map<String, String>?) {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            MainActivity.newInstance(context, data),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_vietalk)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setLargeIcon(loadBitmapFromUri(context, imageUri))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        val inputStream = uri?.let { URL(it.toString()).openStream() }
        if (inputStream == null) {
            return BitmapFactory.decodeResource(context.resources, R.drawable.ic_person_color)
        }
        return inputStream.use { BitmapFactory.decodeStream(it) }
    }
}

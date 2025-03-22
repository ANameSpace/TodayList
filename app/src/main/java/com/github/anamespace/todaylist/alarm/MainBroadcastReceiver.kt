package com.github.anamespace.todaylist.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MainBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "scheduled_notification_channel"
        const val CHANNEL_NAME = "Scheduled Notifications"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_NOTIFICATION_TITLE = "extra_notification_title"
        const val EXTRA_NOTIFICATION_TEXT = "extra_notification_text"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return

        val title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE) ?: "Уведомление"
        val text = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT) ?: "уведомление"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
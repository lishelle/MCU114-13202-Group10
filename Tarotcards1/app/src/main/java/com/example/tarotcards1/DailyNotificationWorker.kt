package com.example.tarotcards1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "daily_tarot_reminder",
                "每日塔羅提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "每日運勢占卜提醒"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to open the app when the notification is tapped
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        val builder = NotificationCompat.Builder(applicationContext, "daily_tarot_reminder")
            .setSmallIcon(R.drawable.ic_notification) // You need to create this icon
            .setContentTitle("每日塔羅提醒")
            .setContentText("趕緊占卜看看今日的運勢")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(1, builder.build())

        return Result.success()
    }
}
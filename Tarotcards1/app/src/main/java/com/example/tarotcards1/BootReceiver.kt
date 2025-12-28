package com.example.tarotcards1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("app", 0)
            val shouldNotify = prefs.getBoolean("notify_on", false) // Only reschedule if it was on

            if (shouldNotify) {
                val workManager = WorkManager.getInstance(context)

                // The same scheduling logic as in SettingsFragment
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }
                val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

                val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
                    24, TimeUnit.HOURS
                ).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                 .build()

                workManager.enqueueUniquePeriodicWork(
                    "daily_notification",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    dailyWorkRequest
                )
            }
        }
    }
}

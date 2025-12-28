package com.example.tarotcards1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SettingsFragment : TrackedFragment("Settings") {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        val prefs = requireActivity().getSharedPreferences("app", 0)
        val workManager = WorkManager.getInstance(requireContext())

        val sw = v.findViewById<Switch>(R.id.swNotify)
        sw.isChecked = prefs.getBoolean("notify_on", true)
        sw.setOnCheckedChangeListener { _, isChecked ->
            logClick("notify_toggle_$isChecked")
            prefs.edit().putBoolean("notify_on", isChecked).apply()

            if (isChecked) {
                // Schedule the daily notification
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
            } else {
                // Cancel the daily notification
                workManager.cancelUniqueWork("daily_notification")
            }
        }
        return v
    }
}

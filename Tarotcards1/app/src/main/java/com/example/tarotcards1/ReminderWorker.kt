package com.example.tarotcards1

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {
    override fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("app", 0)
        val on = prefs.getBoolean("notify_on", true)
        Log.d("ReminderWorker", "Daily task run. notify_on=$on")
        return Result.success()
    }
}

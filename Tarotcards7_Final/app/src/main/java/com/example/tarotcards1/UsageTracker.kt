package com.example.tarotcards1

import android.content.Context
import androidx.fragment.app.Fragment

open class TrackedFragment(private val pageName: String) : Fragment() {

    private var enterTs: Long = 0

    override fun onResume() {
        super.onResume()
        enterTs = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        val exitTs = System.currentTimeMillis()
        
        context?.let {
            val prefs = it.getSharedPreferences("app", Context.MODE_PRIVATE)
            val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)
            
            if (userId != -1L) {
                AppDb.getInstance(it).insertUsage(userId, pageName, "stay", enterTs, exitTs)
            }
        }
    }

    fun logClick(action: String) {
        val now = System.currentTimeMillis()
        
        context?.let {
            val prefs = it.getSharedPreferences("app", Context.MODE_PRIVATE)
            val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

            if (userId != -1L) {
                AppDb.getInstance(it).insertUsage(userId, pageName, action, now, now)
            }
        }
    }
}

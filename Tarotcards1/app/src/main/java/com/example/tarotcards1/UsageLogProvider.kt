package com.example.tarotcards1

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

class UsageLogProvider : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // Use context safely, returning null if it's not available.
        val safeContext = context ?: return null

        val prefs = safeContext.getSharedPreferences("app", Context.MODE_PRIVATE)
        val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

        return if (userId != -1L) {
            AppDb.getInstance(safeContext).queryUsageLogs(userId)
        } else {
            null // Or return an empty cursor
        }
    }


    override fun getType(uri: Uri): String = "vnd.android.cursor.dir/vnd.tarot.usage_logs"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0


}

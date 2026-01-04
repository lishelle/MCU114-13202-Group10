package com.example.tarotcards1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppDb private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, "app.db", null, 2) {

    companion object {
        const val USER_ID_KEY = "current_user_id"

        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = AppDb(context)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                ts INTEGER,
                mode INTEGER,
                result TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE usage_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                page TEXT,
                action_name TEXT,
                start_ts INTEGER,
                end_ts INTEGER,
                duration_ms INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE feedback (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                ts INTEGER,
                message TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE history ADD COLUMN user_id INTEGER")
            db.execSQL("ALTER TABLE usage_logs ADD COLUMN user_id INTEGER")
            db.execSQL("ALTER TABLE feedback ADD COLUMN user_id INTEGER")
        }
    }

    // -------- Users --------
    fun register(username: String, password: String): Boolean {
        return try {
            val cv = ContentValues().apply {
                put("username", username)
                put("password", password)
            }
            writableDatabase.insertOrThrow("user", null, cv)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun login(username: String, password: String): Long? {
        val c = readableDatabase.rawQuery(
            "SELECT id FROM user WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        var userId: Long? = null
        if (c.moveToFirst()) {
            userId = c.getLong(0)
        }
        c.close()
        return userId
    }

    // -------- History --------
    fun insertHistory(userId: Long, mode: Int, result: String) {
        val cv = ContentValues().apply {
            put("user_id", userId)
            put("ts", System.currentTimeMillis())
            put("mode", mode)
            put("result", result)
        }
        writableDatabase.insert("history", null, cv)
    }

    fun queryHistory(userId: Long): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase
        val c = db.rawQuery(
            "SELECT ts, mode, result FROM history WHERE user_id=? ORDER BY id DESC",
            arrayOf(userId.toString())
        )
        
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val titles = listOf("過去", "現在", "未來")

        while (c.moveToNext()) {
            val timestamp = c.getLong(0)
            val mode = c.getInt(1)
            val result = c.getString(2)
            val dateString = sdf.format(Date(timestamp))

            if (mode == 3) {
                val readings = result.split("\n\n").mapIndexed { index, reading ->
                    if (index < titles.size) "${titles[index]}：$reading" else reading
                }.joinToString("\n\n")
                list.add("$dateString\n$readings")
            } else {
                list.add("$dateString\n$result")
            }
        }

        c.close()
        return list
    }

    // -------- Usage Logs --------
    fun insertUsage(
        userId: Long,
        page: String,
        actionName: String,
        startTs: Long,
        endTs: Long
    ) {
        val cv = ContentValues().apply {
            put("user_id", userId)
            put("page", page)
            put("action_name", actionName)
            put("start_ts", startTs)
            put("end_ts", endTs)
            put("duration_ms", endTs - startTs)
        }
        writableDatabase.insert("usage_logs", null, cv)
    }

    fun queryUsageLogs(userId: Long): Cursor {
        return readableDatabase.rawQuery(
            "SELECT * FROM usage_logs WHERE user_id=? ORDER BY id DESC",
            arrayOf(userId.toString())
        )
    }

    // -------- Feedback --------
    fun insertFeedback(userId: Long, message: String) {
        val cv = ContentValues().apply {
            put("user_id", userId)
            put("ts", System.currentTimeMillis())
            put("message", message)
        }
        writableDatabase.insert("feedback", null, cv)
    }
}

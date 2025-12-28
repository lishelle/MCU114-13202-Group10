package com.example.lab15

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 自訂建構子並繼承 SQLiteOpenHelper 類別
class MyDBHelper(
    context: Context,
    name: String = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = VERSION
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val DB_NAME = "carDatabase"
        private const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE carTable (
                brand TEXT PRIMARY KEY,
                year INTEGER NOT NULL,
                price INTEGER NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS carTable")
        onCreate(db)
    }
}
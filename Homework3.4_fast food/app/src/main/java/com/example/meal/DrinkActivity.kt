package com.example.meal

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        // 找按鈕
        val btnCola = findViewById<Button>(R.id.btnCola)
        val btnJuice = findViewById<Button>(R.id.btnJuice)

        // 點「可樂」
        btnCola.setOnClickListener {
            MainActivity.drink = "可樂"
            finish()
        }

        // 點「果汁」
        btnJuice.setOnClickListener {
            MainActivity.drink = "果汁"
            finish()
        }
    }
}

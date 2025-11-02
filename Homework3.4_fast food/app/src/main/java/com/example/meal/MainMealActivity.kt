package com.example.meal

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // 用 findViewById 找按鈕
        val btnBurger = findViewById<Button>(R.id.btnBurger)
        val btnChicken = findViewById<Button>(R.id.btnChicken)

        btnBurger.setOnClickListener {
            MainActivity.mainMeal = "漢堡"
            finish()
        }

        btnChicken.setOnClickListener {
            MainActivity.mainMeal = "炸雞"
            finish()
        }
    }
}


package com.example.meal

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SideDishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        // 取得按鈕
        val btnFries = findViewById<Button>(R.id.btnFries)
        val btnSalad = findViewById<Button>(R.id.btnSalad)

        // 點下「薯條」
        btnFries.setOnClickListener {
            MainActivity.sideDish = "薯條"
            finish() // 返回主畫面
        }

        // 點下「沙拉」
        btnSalad.setOnClickListener {
            MainActivity.sideDish = "沙拉"
            finish()
        }
    }
}

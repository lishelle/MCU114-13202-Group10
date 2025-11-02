package com.example.meal

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        val txtConfirm = findViewById<TextView>(R.id.txtConfirm)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // 顯示訂單內容
        val summary = StringBuilder()
        summary.append("您的餐點如下：\n\n")

        summary.append("主餐：${MainActivity.mainMeal ?: "未選擇"}\n")
        summary.append("副餐：${MainActivity.sideDish ?: "未選擇"}\n")
        summary.append("飲料：${MainActivity.drink ?: "未選擇"}\n")

        txtConfirm.text = summary.toString()

        // 返回主畫面
        btnBack.setOnClickListener {
            finish()
        }
    }
}

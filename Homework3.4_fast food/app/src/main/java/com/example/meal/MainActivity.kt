package com.example.meal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        var mainMeal: String? = null
        var sideDish: String? = null
        var drink: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 取得元件
        val txtOrder = findViewById<TextView>(R.id.txtOrder)
        val btnMainMeal = findViewById<Button>(R.id.btnMainMeal)
        val btnSideDish = findViewById<Button>(R.id.btnSideDish)
        val btnDrink = findViewById<Button>(R.id.btnDrink)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)

        // 點擊進入各畫面
        btnMainMeal.setOnClickListener {
            startActivity(Intent(this, MainMealActivity::class.java))
        }

        btnSideDish.setOnClickListener {
            startActivity(Intent(this, SideDishActivity::class.java))
        }

        btnDrink.setOnClickListener {
            startActivity(Intent(this, DrinkActivity::class.java))
        }

        btnConfirm.setOnClickListener {
            startActivity(Intent(this, ConfirmActivity::class.java))
        }

        // 更新目前選擇（onCreate 只顯示初始值）
        val summary = StringBuilder()
        if (mainMeal != null) summary.append("主餐: $mainMeal\n")
        if (sideDish != null) summary.append("副餐: $sideDish\n")
        if (drink != null) summary.append("飲料: $drink\n")
        txtOrder.text = if (summary.isEmpty()) "尚未點餐" else summary.toString()
    }

    override fun onResume() {
        super.onResume()

        // 回來主頁時更新文字
        val txtOrder = findViewById<TextView>(R.id.txtOrder)
        val summary = StringBuilder()
        if (mainMeal != null) summary.append("主餐: $mainMeal\n")
        if (sideDish != null) summary.append("副餐: $sideDish\n")
        if (drink != null) summary.append("飲料: $drink\n")

        txtOrder.text = if (summary.isEmpty()) "尚未點餐" else summary.toString()
    }
}


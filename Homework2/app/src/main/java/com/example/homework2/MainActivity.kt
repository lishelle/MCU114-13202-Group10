package com.example.homework2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // 讓整個 Activity 都能使用這個 TextView
    private lateinit var tvNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定安全區域（原有代碼保留）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 取得元件
        tvNumber = findViewById(R.id.tvNumber)

        val btn1 = findViewById<Button>(R.id.button)
        val btn2 = findViewById<Button>(R.id.button2)
        val btn3 = findViewById<Button>(R.id.button3)
        val btn4 = findViewById<Button>(R.id.button4)
        val btn5 = findViewById<Button>(R.id.button5)
        val btn6 = findViewById<Button>(R.id.button6)
        val btn7 = findViewById<Button>(R.id.button7)
        val btn8 = findViewById<Button>(R.id.button8)
        val btn9 = findViewById<Button>(R.id.button9)
        val btnStar = findViewById<Button>(R.id.button10)
        val btn0 = findViewById<Button>(R.id.button11)
        val btnClear = findViewById<Button>(R.id.button12)

        // 讓所有按鈕共用同一個監聽器（模仿 Homework2）
        btn1.setOnClickListener(myListener)
        btn2.setOnClickListener(myListener)
        btn3.setOnClickListener(myListener)
        btn4.setOnClickListener(myListener)
        btn5.setOnClickListener(myListener)
        btn6.setOnClickListener(myListener)
        btn7.setOnClickListener(myListener)
        btn8.setOnClickListener(myListener)
        btn9.setOnClickListener(myListener)
        btnStar.setOnClickListener(myListener)
        btn0.setOnClickListener(myListener)
        btnClear.setOnClickListener(myListener)
    }

    // === 共用監聽器 ===
    private val myListener = View.OnClickListener { v ->
        val s = tvNumber.text.toString() // 目前顯示的字串
        when (v.id) {
            R.id.button -> tvNumber.text = s + "1"
            R.id.button2 -> tvNumber.text = s + "2"
            R.id.button3 -> tvNumber.text = s + "3"
            R.id.button4 -> tvNumber.text = s + "4"
            R.id.button5 -> tvNumber.text = s + "5"
            R.id.button6 -> tvNumber.text = s + "6"
            R.id.button7 -> tvNumber.text = s + "7"
            R.id.button8 -> tvNumber.text = s + "8"
            R.id.button9 -> tvNumber.text = s + "9"
            R.id.button10 -> tvNumber.text = s + "*"
            R.id.button11 -> tvNumber.text = s + "0"
            R.id.button12 -> tvNumber.text = ""
        }
    }
}

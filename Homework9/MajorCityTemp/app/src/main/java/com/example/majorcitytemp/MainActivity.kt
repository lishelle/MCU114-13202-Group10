package com.example.majorcitytemp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // 資料類別（解析 JSON）
    data class WeatherResponse(val main: MainData, val name: String)
    data class MainData(val temp: Double, val humidity: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGetTemp = findViewById<Button>(R.id.btnGetTemp)

        // ➤ 城市清單（顯示中文，查詢用英文）
        btnGetTemp.setOnClickListener {
            val cities = listOf(
                "台北" to "Taipei",
                "新北" to "New Taipei City",
                "桃園" to "Taoyuan City",
                "新竹" to "Hsinchu City",
                "苗栗" to "Miaoli County",
                "台中" to "Taichung City",
                "彰化" to "Changhua County",
                "南投" to "Nantou County",
                "雲林" to "Yunlin County",
                "嘉義" to "Chiayi City",
                "台南" to "Tainan City",
                "高雄" to "Kaohsiung City",
                "屏東" to "Pingtung County",
                "宜蘭" to "Yilan County",
                "花蓮" to "Hualien County",
                "台東" to "Taitung County",
                "基隆" to "Keelung City",
                "澎湖" to "Penghu County",
                "金門" to "Jingmen County",
                "連江" to "Lianjiang County"
            )

            AlertDialog.Builder(this)
                .setTitle("選擇城市")
                .setItems(cities.map { it.first }.toTypedArray()) { _, which ->
                    val queryCity = cities[which].second   // 英文名稱用來查詢 API
                    fetchWeather(queryCity)
                }
                .show()
        }
    }

    // ➤ 接收城市名稱查詢天氣（放在 class 裡，不在 onCreate 裡）
    private fun fetchWeather(city: String) {
        val apiKey = "7bed417e5f6eb26944d478f84792fac1" // 你的 API KEY
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey"

        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData = response.body?.string()

                    if (responseData != null) {
                        val gson = Gson()
                        val weatherData = gson.fromJson(responseData, WeatherResponse::class.java)

                        withContext(Dispatchers.Main) {
                            showAlertDialog(
                                "Current Weather",
                                "City: ${weatherData.name}\n" +
                                        "Temperature: ${weatherData.main.temp}°C\n" +
                                        "Humidity: ${weatherData.main.humidity}%"
                            )
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showAlertDialog("Error", "Failed to get data. Code: ${response.code}")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    showAlertDialog("Error", "Network error: ${e.message}")
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}

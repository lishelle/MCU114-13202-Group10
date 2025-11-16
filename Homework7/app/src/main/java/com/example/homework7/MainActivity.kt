package com.example.homework7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import android.graphics.Color
import com.google.maps.android.PolyUtil

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {

        val taipei101 = LatLng(25.033611, 121.565000)
        val taipeiMain = LatLng(25.047924, 121.517081)

        map.addMarker(MarkerOptions().position(taipei101).title("台北 101"))
        map.addMarker(MarkerOptions().position(taipeiMain).title("台北車站"))

        val key = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.google.android.geo.API_KEY")

        // 🔥 用 API URL 呼叫 Directions（Android可用）
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=${taipei101.latitude},${taipei101.longitude}" +
                    "&destination=${taipeiMain.latitude},${taipeiMain.longitude}" +
                    "&mode=walking&key=$key"

        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val json = response.body?.string() ?: return@launch

            val jsonObj = JSONObject(json)
            val points = jsonObj
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points")

            val path = PolyUtil.decode(points)

            withContext(Dispatchers.Main) {
                map.addPolyline(
                    PolylineOptions()
                        .addAll(path)
                        .color(Color.RED)
                        .width(12f)
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(taipei101, 13f))
            }
        }
    }
}

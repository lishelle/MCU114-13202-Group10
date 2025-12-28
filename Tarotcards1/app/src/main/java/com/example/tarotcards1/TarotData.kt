package com.example.tarotcards1

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class TarotData(context: Context) {

    private val cards = mutableMapOf<String, JSONObject>()

    init {
        val jsonString = context.assets.open("tarot_data.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val card = jsonArray.getJSONObject(i)
            cards[card.getString("name_en")] = card
        }
    }

    fun getCard(nameEn: String): JSONObject? {
        return cards[nameEn]
    }
}

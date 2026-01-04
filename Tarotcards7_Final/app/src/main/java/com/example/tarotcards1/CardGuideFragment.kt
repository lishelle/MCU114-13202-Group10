package com.example.tarotcards1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

data class TarotCard(val name: String, val shortName: String)

class CardGuideFragment : TrackedFragment("CardGuide") {

    private lateinit var listView: ListView
    private lateinit var loadingSpinner: ProgressBar
    private val cardList = mutableListOf<TarotCard>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_card_guide, container, false)
        listView = v.findViewById(R.id.card_list_view)
        loadingSpinner = v.findViewById(R.id.loading_spinner)

        // Fetch the list of all cards
        fetchAllCards()

        // Set up click listener to navigate to detail view
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCard = cardList[position]
            val fragment = CardDetailFragment.newInstance(selectedCard.shortName)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return v
    }

    private fun fetchAllCards() {
        loadingSpinner.visibility = View.VISIBLE
        thread {
            try {
                val url = URL("https://tarotapi.dev/api/v1/cards")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 8000
                    readTimeout = 8000
                }

                if (conn.responseCode == 200) {
                    val json = conn.inputStream.bufferedReader().readText()
                    // Correctly parse the JSON array
                    val cardsArray = JSONObject(json).getJSONArray("cards")

                    for (i in 0 until cardsArray.length()) {
                        val cardObj = cardsArray.getJSONObject(i)
                        cardList.add(TarotCard(cardObj.getString("name"), cardObj.getString("name_short")))
                    }

                    activity?.runOnUiThread {
                        loadingSpinner.visibility = View.GONE
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cardList.map { it.name })
                        listView.adapter = adapter
                    }
                } else {
                    showError()
                }
            } catch (e: Exception) {
                showError()
            }
        }
    }

    private fun showError() {
        activity?.runOnUiThread {
            loadingSpinner.visibility = View.GONE
            Toast.makeText(context, "無法載入卡牌列表，請檢查網路連線。", Toast.LENGTH_LONG).show()
        }
    }
}

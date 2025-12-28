package com.example.tarotcards1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class CardDetailFragment : TrackedFragment("CardDetail") {

    private lateinit var cardShortName: String

    companion object {
        private const val ARG_CARD_SHORT_NAME = "card_short_name"

        fun newInstance(cardShortName: String): CardDetailFragment {
            val fragment = CardDetailFragment()
            val args = Bundle()
            args.putString(ARG_CARD_SHORT_NAME, cardShortName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardShortName = arguments?.getString(ARG_CARD_SHORT_NAME) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_card_detail, container, false)
        fetchAndDisplayCardDetails(v)
        return v
    }

    private fun fetchAndDisplayCardDetails(view: View) {
        val loadingSpinner = view.findViewById<ProgressBar>(R.id.detail_loading_spinner)
        val nameTextView = view.findViewById<TextView>(R.id.card_name_text)
        val meaningUpTextView = view.findViewById<TextView>(R.id.meaning_up_text)
        val meaningRevTextView = view.findViewById<TextView>(R.id.meaning_rev_text)
        
        loadingSpinner.visibility = View.VISIBLE

        thread {
            try {
                val url = URL("https://tarotapi.dev/api/v1/cards/$cardShortName")
                val conn = (url.openConnection() as HttpURLConnection)
                conn.connectTimeout = 8000
                conn.readTimeout = 8000

                if (conn.responseCode == 200) {
                    val json = conn.inputStream.bufferedReader().readText()
                    val card = JSONObject(json).getJSONArray("cards").getJSONObject(0)

                    val name = card.getString("name")
                    val meaningUp = card.getString("meaning_up")
                    val meaningRev = card.getString("meaning_rev")

                    activity?.runOnUiThread {
                        nameTextView.text = name
                        meaningUpTextView.text = meaningUp
                        meaningRevTextView.text = meaningRev
                        loadingSpinner.visibility = View.GONE
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
            view?.findViewById<ProgressBar>(R.id.detail_loading_spinner)?.visibility = View.GONE
            Toast.makeText(context, "Could not load card details. Please check connection.", Toast.LENGTH_LONG).show()
        }
    }
}

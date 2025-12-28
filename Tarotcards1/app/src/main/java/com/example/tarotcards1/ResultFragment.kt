package com.example.tarotcards1

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ResultFragment : TrackedFragment("Result") {

    companion object {
        fun newInstance(mode: Int): ResultFragment {
            val f = ResultFragment()
            f.arguments = Bundle().apply { putInt("mode", mode) }
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_result, container, false)
        val tv = v.findViewById<TextView>(R.id.tvResult)
        val mode = arguments?.getInt("mode") ?: 1
        val db = AppDb.getInstance(requireContext())

        val prefs = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE)
        val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

        tv.text = "Fetching card(s)..."

        fetchAndShowCards(
            count = mode,
            onOk = { resultText ->
                requireActivity().runOnUiThread {
                    tv.text = resultText
                    if (userId != -1L) db.insertHistory(userId, mode, resultText)
                }
            },
            onFail = {
                requireActivity().runOnUiThread {
                    tv.text = "Could not fetch cards. Please check your network connection."
                }
            }
        )

        v.findViewById<Button>(R.id.btnSaveBack).setOnClickListener {
            logClick("back_home")
            parentFragmentManager.popBackStack()
        }

        return v
    }

    private fun fetchAndShowCards(count: Int, onOk: (String) -> Unit, onFail: () -> Unit) {
        thread {
            try {
                val url = URL("https://tarotapi.dev/api/v1/cards/random?n=$count")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 8000
                    readTimeout = 8000
                }
                if (conn.responseCode != 200) { onFail(); return@thread }

                val json = conn.inputStream.bufferedReader().readText()
                val cards = JSONObject(json).getJSONArray("cards")
                val resultBuilder = StringBuilder()

                if (count == 3) {
                    resultBuilder.append("Past / Present / Future:\n\n")
                }

                for (i in 0 until cards.length()) {
                    val card = cards.getJSONObject(i)
                    val name = card.optString("name", "Unknown")
                    val meaning = card.optString("meaning_up", "(No meaning found)")
                    resultBuilder.append("Name: $name\n\nMeaning: $meaning\n\n")
                }

                onOk(resultBuilder.toString().trim())

            } catch (e: Exception) {
                onFail()
            }
        }
    }
}

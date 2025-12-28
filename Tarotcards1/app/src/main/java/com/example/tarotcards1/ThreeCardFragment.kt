package com.example.tarotcards1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tarotcards1.databinding.FragmentThreeCardBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.random.Random

class ThreeCardFragment : Fragment() {

    private var _binding: FragmentThreeCardBinding? = null
    private val binding get() = _binding!!
    private var drawnCards: JSONArray? = null
    private lateinit var tarotData: TarotData

    companion object {
        fun newInstance() = ThreeCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreeCardBinding.inflate(inflater, container, false)
        tarotData = TarotData(requireContext())

        binding.btnDrawCards.setOnClickListener { fetchAndDisplayCards() }
        binding.btnCardMeanings.setOnClickListener { showCardMeanings() }

        return binding.root
    }

    private fun fetchAndDisplayCards() {
        binding.loadingSpinner.visibility = View.VISIBLE
        binding.btnDrawCards.visibility = View.GONE

        val safeContext = context?.applicationContext ?: return
        val prefs = safeContext.getSharedPreferences("app", Context.MODE_PRIVATE)
        val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

        thread {
            try {
                val url = URL("https://tarotapi.dev/api/v1/cards/random?n=3")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                if (conn.responseCode == 200) {
                    val json = conn.inputStream.bufferedReader().readText()
                    val cardsArray = JSONObject(json).getJSONArray("cards")
                    val cardsWithReversed = JSONArray()
                    for (i in 0 until cardsArray.length()) {
                        val card = cardsArray.getJSONObject(i)
                        card.put("reversed", Random.nextBoolean())
                        cardsWithReversed.put(card)
                    }
                    drawnCards = cardsWithReversed

                    saveToHistory(safeContext, userId, drawnCards!!)

                    activity?.runOnUiThread {
                        if (_binding == null) return@runOnUiThread
                        updateCardUI(drawnCards!!)
                        binding.loadingSpinner.visibility = View.GONE
                        binding.btnCardMeanings.visibility = View.VISIBLE
                    }
                } else {
                    showError("無法獲取卡牌，請檢查網路連線。")
                }
            } catch (e: Exception) {
                Log.e("ThreeCardFragment", "Error fetching cards: ", e)
                showError("抽牌時發生錯誤，請稍後再試。")
            }
        }
    }

    private fun updateCardUI(cards: JSONArray) {
        val slots = listOf(
            Triple(binding.imagePast, binding.textPast, "過去"),
            Triple(binding.imagePresent, binding.textPresent, "現在"),
            Triple(binding.imageFuture, binding.textFuture, "未來")
        )

        for (i in 0 until cards.length()) {
            val card = cards.getJSONObject(i)
            val nameEn = card.optString("name", "Unknown Card")
            val localCardData = tarotData.getCard(nameEn)

            val name = localCardData?.optString("name_cn", nameEn) ?: nameEn
            val isReversed = card.optBoolean("reversed", false)
            val orientation = if (isReversed) "(逆位)" else "(正位)"

            slots[i].first.setImageResource(R.drawable.card777)
            slots[i].first.rotation = if(isReversed) 180f else 0f
            slots[i].second.text = "${slots[i].third}\n$name $orientation"
        }
    }

    private fun showCardMeanings() {
        drawnCards?.let {
            val meaningsArray = JSONArray()
            for (i in 0 until it.length()) {
                val card = it.getJSONObject(i)
                val nameEn = card.optString("name", "Unknown Card")
                val localCardData = tarotData.getCard(nameEn)
                val meaning = if (card.optBoolean("reversed", false)) {
                    localCardData?.optString("meaning_reversed", "牌義不詳")
                } else {
                    localCardData?.optString("meaning_upright", "牌義不詳")
                }

                val cardForMeaning = JSONObject().apply {
                    put("name", localCardData?.optString("name_cn", nameEn) ?: nameEn)
                    put("reversed", card.optBoolean("reversed", false))
                    put("meaning_up", meaning)
                    put("meaning_rev", meaning)
                }
                meaningsArray.put(cardForMeaning)
            }

            val fragment = CardMeaningsFragment.newInstance(meaningsArray.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun saveToHistory(context: Context, userId: Long, cards: JSONArray) {
        if (userId != -1L) {
            val historyText = (0 until cards.length()).joinToString(separator = "\n\n") { i ->
                val card = cards.getJSONObject(i)
                val nameEn = card.optString("name", "Unknown Card")
                val localCardData = tarotData.getCard(nameEn)
                val name = localCardData?.optString("name_cn", nameEn) ?: nameEn
                val isReversed = card.optBoolean("reversed", false)
                val orientation = if (isReversed) "(逆位)" else "(正位)"
                val meaning = if (isReversed) {
                    localCardData?.optString("meaning_reversed", "牌義不詳")
                } else {
                    localCardData?.optString("meaning_upright", "牌義不詳")
                }
                "$name $orientation\n$meaning"
            }
            AppDb.getInstance(context).insertHistory(userId, 3, historyText)
        }
    }

    private fun showError(message: String) {
        activity?.runOnUiThread {
            val currentContext = context
            if (_binding == null || currentContext == null) return@runOnUiThread
            binding.loadingSpinner.visibility = View.GONE
            binding.btnDrawCards.visibility = View.VISIBLE
            Toast.makeText(currentContext, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

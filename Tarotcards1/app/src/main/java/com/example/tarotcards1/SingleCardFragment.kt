package com.example.tarotcards1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tarotcards1.databinding.FragmentSingleCardBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.random.Random

class SingleCardFragment : Fragment() {

    private var _binding: FragmentSingleCardBinding? = null
    private val binding get() = _binding!!
    private var drawnCard: JSONObject? = null
    private lateinit var tarotData: TarotData

    companion object {
        fun newInstance() = SingleCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleCardBinding.inflate(inflater, container, false)
        tarotData = TarotData(requireContext())

        binding.btnDrawCard.setOnClickListener { fetchAndDisplayCard() }
        binding.btnCardMeaning.setOnClickListener { showCardMeaning() }

        return binding.root
    }

    private fun fetchAndDisplayCard() {
        binding.loadingSpinner.visibility = View.VISIBLE
        binding.btnDrawCard.visibility = View.GONE

        val safeContext = context?.applicationContext ?: return
        val prefs = safeContext.getSharedPreferences("app", Context.MODE_PRIVATE)
        val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

        thread {
            try {
                val url = URL("https://tarotapi.dev/api/v1/cards/random?n=1")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                if (conn.responseCode == 200) {
                    val json = conn.inputStream.bufferedReader().readText()
                    val cardsArray = JSONObject(json).getJSONArray("cards")

                    if (cardsArray.length() > 0) {
                        val card = cardsArray.getJSONObject(0)
                        card.put("reversed", Random.nextBoolean())
                        drawnCard = card

                        saveToHistory(safeContext, userId, drawnCard!!)

                        activity?.runOnUiThread {
                            if (_binding == null) return@runOnUiThread
                            updateCardUI(drawnCard!!)
                            binding.loadingSpinner.visibility = View.GONE
                            binding.btnCardMeaning.visibility = View.VISIBLE
                        }
                    } else {
                        showError("無法獲取卡牌資料，請稍後再試。")
                    }
                } else {
                    showError("無法獲取卡牌，請檢查網路連線。")
                }
            } catch (e: Exception) {
                Log.e("SingleCardFragment", "Error fetching card: ", e)
                showError("抽牌時發生錯誤，請稍後再試。")
            }
        }
    }

    private fun updateCardUI(card: JSONObject) {
        val nameEn = card.optString("name", "Unknown Card")
        val localCardData = tarotData.getCard(nameEn)

        val name = localCardData?.optString("name_cn", nameEn) ?: nameEn
        val isReversed = card.optBoolean("reversed", false)
        val orientation = if (isReversed) "(逆位)" else "(正位)"

        binding.imageCard.setImageResource(R.drawable.card777)
        binding.imageCard.rotation = if (isReversed) 180f else 0f
        binding.textCardName.text = "$name $orientation"
    }

    private fun showCardMeaning() {
        drawnCard?.let {
            val nameEn = it.optString("name", "Unknown Card")
            val localCardData = tarotData.getCard(nameEn)
            val meaning = if (it.optBoolean("reversed", false)) {
                localCardData?.optString("meaning_reversed", "牌義不詳")
            } else {
                localCardData?.optString("meaning_upright", "牌義不詳")
            }

            val cardForMeaning = JSONObject().apply {
                put("name", localCardData?.optString("name_cn", nameEn) ?: nameEn)
                put("reversed", it.optBoolean("reversed", false))
                put("meaning_up", meaning)
                put("meaning_rev", meaning) // Since we already have the specific meaning, we can put it in both.
            }

            val cardsArray = JSONArray().apply { put(cardForMeaning) }
            val fragment = CardMeaningsFragment.newInstance(cardsArray.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun saveToHistory(context: Context, userId: Long, card: JSONObject) {
        if (userId != -1L) {
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
            val historyText = "$name $orientation\n$meaning"
            AppDb.getInstance(context).insertHistory(userId, 1, historyText)
        }
    }

    private fun showError(message: String) {
        activity?.runOnUiThread {
            val currentContext = context
            if (_binding == null || currentContext == null) return@runOnUiThread
            binding.loadingSpinner.visibility = View.GONE
            binding.btnDrawCard.visibility = View.VISIBLE
            Toast.makeText(currentContext, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

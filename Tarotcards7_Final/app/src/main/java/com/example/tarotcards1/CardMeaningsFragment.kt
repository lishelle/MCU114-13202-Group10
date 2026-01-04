package com.example.tarotcards1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONArray

class CardMeaningsFragment : Fragment() {

    companion object {
        private const val ARG_CARDS_JSON = "cards_json"

        fun newInstance(cardsJson: String): CardMeaningsFragment {
            val fragment = CardMeaningsFragment()
            val args = Bundle()
            args.putString(ARG_CARDS_JSON, cardsJson)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_card_meanings, container, false)

        val meaningsContainer = view.findViewById<LinearLayout>(R.id.meanings_container)
        val redrawButton = view.findViewById<Button>(R.id.btn_redraw)
        val backToHomeButton = view.findViewById<Button>(R.id.btn_back_to_home)
        val fortuneButton = view.findViewById<Button>(R.id.btn_copy_for_gemini)

        val cardsJson = arguments?.getString(ARG_CARDS_JSON)
            ?: return view

        val cards = JSONArray(cardsJson)


        addMeaningViews(inflater, meaningsContainer, cards)

        fortuneButton.setOnClickListener {


            if (cards.length() == 1) {

                val card = cards.getJSONObject(0)
                val name = card.optString("name", "牌名不詳")
                val isReversed = card.optBoolean("reversed", false)

                val advice = TarotDailyAdvice.getAdvice(name, isReversed)

                meaningsContainer.removeAllViews()

                val tv = TextView(requireContext()).apply {
                    text = "今日建議：\n$advice"
                    background = requireContext().getDrawable(R.drawable.meaning_box_background)
                    setTextColor(resources.getColor(android.R.color.white, null))
                    setPadding(16.dpToPx())
                }

                meaningsContainer.addView(tv)


                return@setOnClickListener
            }


            val textForQuery = (0 until cards.length()).joinToString("\n") { i ->
                val card = cards.getJSONObject(i)
                val name = card.optString("name", "牌名不詳")
                val isReversed = card.optBoolean("reversed", false)
                val orientation = if (isReversed) "(逆位)" else "(正位)"
                val title = "${listOf("過去", "現在", "未來")[i]}： "
                "$title$name $orientation"
            }

            val fullPrompt =
                "請用塔羅牌的牌義，為我解讀以下抽牌結果：\n\n$textForQuery"

            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(
                ClipData.newPlainText("Tarot Reading for Gemini", fullPrompt)
            )

            Toast.makeText(requireContext(), "已複製解讀請求至剪貼簿", Toast.LENGTH_SHORT).show()

            val geminiUrl =
                "https://gemini.google.com/?q=" + Uri.encode(fullPrompt)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(geminiUrl)))
        }

        redrawButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        backToHomeButton.setOnClickListener {
            parentFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        return view
    }

    private fun addMeaningViews(
        inflater: LayoutInflater,
        container: LinearLayout,
        cards: JSONArray
    ) {
        val titles = listOf("過去", "現在", "未來")

        for (i in 0 until cards.length()) {
            val card = cards.getJSONObject(i)
            val name = card.optString("name", "牌名不詳")
            val isReversed = card.optBoolean("reversed", false)
            val orientation = if (isReversed) "(逆位)" else "(正位)"

            val meaning =
                if (isReversed)
                    card.optString("meaning_rev", "牌義不詳")
                else
                    card.optString("meaning_up", "牌義不詳")

            val cardMeaning =
                if (cards.length() > 1)
                    "${titles[i]}：$name $orientation\n$meaning"
                else
                    "$name $orientation\n$meaning"

            val textView = TextView(requireContext()).apply {
                text = cardMeaning
                background =
                    requireContext().getDrawable(R.drawable.meaning_box_background)
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(16.dpToPx())
                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 16.dpToPx()
                    }
            }

            container.addView(textView)
        }
    }

    private fun Int.dpToPx(): Int =
        (this * resources.displayMetrics.density).toInt()
}

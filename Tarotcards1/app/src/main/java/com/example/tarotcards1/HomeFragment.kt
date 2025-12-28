package com.example.tarotcards1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : TrackedFragment("Home") {

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.btn_single_card_reading).setOnClickListener {
            logClick("single_card_reading_click")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SingleCardFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.btn_three_card_reading).setOnClickListener {
            logClick("three_card_reading_click")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ThreeCardFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}

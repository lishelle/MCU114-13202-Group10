package com.example.tarotcards1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_history, container, false)
        val list = v.findViewById<ListView>(R.id.listHistory)
        val db = AppDb.getInstance(requireContext())

        // Get current user ID from SharedPreferences
        val prefs = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE)
        val userId = prefs.getLong(AppDb.USER_ID_KEY, -1) // -1 indicates no user is logged in

        val data = if (userId != -1L) {
            db.queryHistory(userId)
        } else {
            // Handle case where no user is logged in
            Toast.makeText(requireContext(), "Please log in to see your history", Toast.LENGTH_SHORT).show()
            ArrayList<String>() // Return an empty list
        }

        list.adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, data) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(resources.getColor(android.R.color.white, null))
                return view
            }
        }

        return v
    }
}

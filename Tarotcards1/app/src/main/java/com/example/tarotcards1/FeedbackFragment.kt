package com.example.tarotcards1

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FeedbackFragment : TrackedFragment("Feedback") {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_feedback, container, false)
        val et = v.findViewById<EditText>(R.id.etMsg)
        val db = AppDb.getInstance(requireContext())

        v.findViewById<Button>(R.id.btnSend).setOnClickListener {
            logClick("feedback_send")
            val msg = et.text.toString()
            if (msg.isNotBlank()) {
                val prefs = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE)
                val userId = prefs.getLong(AppDb.USER_ID_KEY, -1)

                if (userId != -1L) {
                    db.insertFeedback(userId, msg)
                    Toast.makeText(requireContext(), "已送出", Toast.LENGTH_SHORT).show()
                    et.setText("")
                } else {
                    Toast.makeText(requireContext(), "請先登入再傳送回饋", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "請輸入內容", Toast.LENGTH_SHORT).show()
            }
        }
        return v
    }
}

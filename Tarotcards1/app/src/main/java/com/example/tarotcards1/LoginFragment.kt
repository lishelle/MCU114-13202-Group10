package com.example.tarotcards1

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginFragment : TrackedFragment("Login") {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        val etUser = v.findViewById<EditText>(R.id.etUser)
        val etPass = v.findViewById<EditText>(R.id.etPass)
        val db = AppDb.getInstance(requireContext())

        v.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            logClick("register_click")
            val username = etUser.text.toString()
            val password = etPass.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "請輸入帳號和密碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val ok = db.register(username, password)
            Toast.makeText(requireContext(), if (ok) "註冊成功" else "註冊失敗(帳號可能重複)", Toast.LENGTH_SHORT).show()
        }

        v.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            logClick("login_click")
            val username = etUser.text.toString()
            val password = etPass.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "請輸入帳號和密碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId = db.login(username, password)
            if (userId != null) {
                // Store the user ID on successful login
                val prefs = requireActivity().getSharedPreferences("app", 0)
                prefs.edit().putLong(AppDb.USER_ID_KEY, userId).apply()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            } else {
                Toast.makeText(requireContext(), "登入失敗", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }
}

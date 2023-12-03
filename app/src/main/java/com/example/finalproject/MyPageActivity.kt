package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MyPageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        auth = FirebaseAuth.getInstance()


        val textEmail = findViewById<TextView>(R.id.textEmail)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // 현재 로그인한 사용자 정보를 가져와서 화면에 표시
        val currentUser = auth.currentUser
        if (currentUser != null) {

            textEmail.text = "Email: ${currentUser.email}"
        }

        // 로그아웃 버튼 클릭 시
        btnLogout.setOnClickListener {
            auth.signOut() // Firebase에서 로그아웃
            finish() // 액티비티 종료
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}
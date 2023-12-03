package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {

            val email = findViewById<EditText>(R.id.loginemail).text.toString()
            val password = findViewById<EditText>(R.id.loginpassword).text.toString()
            Login(email, password)
        }
    }

    private fun Login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 시 여기에서 추가 작업 수행 가능
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // 로그인 실패 시 메시지 표시

                }
            }
    }
}
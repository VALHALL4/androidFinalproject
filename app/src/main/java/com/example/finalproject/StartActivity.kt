package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val loginBtn = findViewById<Button>(R.id.login)
        val signupBtn = findViewById<Button>(R.id.SignUp)
        loginBtn.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java))

        }
        signupBtn.setOnClickListener {
            startActivity(
                Intent(this, SignUpActivity::class.java))
        }
    }
}
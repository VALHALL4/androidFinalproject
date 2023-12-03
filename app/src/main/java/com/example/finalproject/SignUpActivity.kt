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
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val signupBtn = findViewById<Button>(R.id.SignUpBtn)

        signupBtn.setOnClickListener {
            var isGoToSingUP = true
            val username = findViewById<EditText>(R.id.username).text.toString()
            val userbirth = findViewById<EditText>(R.id.userBirth).text.toString()
            val email = findViewById<EditText>(R.id.SignUpemail).text.toString()
            val password = findViewById<EditText>(R.id.SignUpPassword).text.toString()
            val passwordCheck = findViewById<EditText>(R.id.chekPassword).text.toString()


            if (username.isEmpty()) {
                Toast.makeText(this, " 이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (userbirth.isEmpty()) {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "password1을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (passwordCheck.isEmpty()) {
                Toast.makeText(this, "password2을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (password.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입략헤주세요.", Toast.LENGTH_LONG).show()
                isGoToSingUP = false
            }
            if (isGoToSingUP) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val userId = auth.currentUser?.uid
                            saveUserDataToFirestore(userId, email, username)
                            Toast.makeText(this, "화원가입 성공", Toast.LENGTH_LONG).show()
                            Login(email,password)

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }



        }
    }
    private fun Login(email: String, password: String) {
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


    private fun saveUserDataToFirestore(userId: String?, email: String, username: String) {
        userId?.let {
            // Firestore에 데이터 저장
            val user = hashMapOf(
                "email" to email,
                "username" to username
                // 여기에 추가적인 사용자 정보를 필요에 따라 추가할 수 있습니다.
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    // Firestore에 데이터 저장 성공
                    // 추가로 필요한 작업을 수행하세요.
                }
                .addOnFailureListener { e ->
                    // Firestore에 데이터 저장 실패
                    // 실패 이유에 대한 처리를 여기에 추가할 수 있습니다.
                }
        }


    }
}
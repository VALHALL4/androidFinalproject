package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.NumberFormatException


class ItemAddActivity: AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        var userName : String
        val collectionReference = firestore.collection("users")
        val uploadBtn = findViewById<Button>(R.id.uploadButton)


        uploadBtn.setOnClickListener {

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userName = currentUser.displayName.toString()
                val email = currentUser.email.toString()
                val title = findViewById<TextView>(R.id.titleEditText).text.toString()
                val content = findViewById<TextView>(R.id.contentEditText).text.toString()
                val priceString = findViewById<TextView>(R.id.priceEditText).text.toString()
                val price: Int = try {
                    priceString.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                // 사용자 이름과 함께 addPost 함수 호출
                addPost(title, userName, content, price,email)
                startActivity(Intent(this, MainActivity::class.java))
            }

        }


    }
    fun addPost(title: String, username: String, content: String, price: Int,email: String) {
        // 게시물 데이터를 맵 형태로 생성
        val postMap = hashMapOf(
            "title" to title,
            "username" to username,
            "content" to content,
            "price" to price,
            "salesstatus" to false,
            "email" to email
        )
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("postdb")
        // 파이어스토어에 새로운 문서 추가
        collectionReference
            .add(postMap)
            .addOnSuccessListener { documentReference ->
                // 성공적으로 추가되면 실행할 코드 (예: 추가된 문서의 ID를 로그에 출력)
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 실패 시 실행할 코드 (예: 에러 로그 출력)
                println("Error adding document: $e")
            }


    }

}
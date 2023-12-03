package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity: AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var postId: String // 게시물의 고유 ID
    private lateinit var auth: FirebaseAuth
    private lateinit var editText :EditText
    private lateinit var sellertext:TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        firestore = FirebaseFirestore.getInstance()
        postId = intent.getStringExtra("postid") ?: ""
        auth = FirebaseAuth.getInstance()
        val documentReference = firestore.collection("postdb").document(postId)
        sellertext = findViewById<TextView>(R.id.textReceiverName)
        editText = findViewById<EditText>(R.id.editTextMessage)
        val sendmsgBtn = findViewById<Button>(R.id.btnSendMessage)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // 문서가 존재하는 경우 데이터 추출
                    val email = documentSnapshot.getString("email") ?: ""
                    sellertext.text = email

                }
                else {

                }
            }
            .addOnFailureListener { e ->
                // 문서 가져오기 실패 시 처리
            // 예: 에러 로그 출력
            }
        sendmsgBtn.setOnClickListener{
            val message = editText.text.toString()
            sendMessage(message)
        }


    }

    private fun sendMessage(message: String) {
        val senderUid = auth.currentUser?.uid
        val senderEmail = auth.currentUser?.email

        // 메시지를 Firestore에 저장
        if (senderUid != null && senderEmail != null) {
            val messageData = hashMapOf(
                "senderUid" to senderUid,
                "senderEmail" to senderEmail,
                "receiverName" to sellertext.text,
                "message" to message,

            )

            // 메시지를 Firestore의 "messages" 컬렉션에 추가
            firestore.collection("messages")
                .add(messageData)
                .addOnSuccessListener { documentReference ->
                    // 성공적으로 메시지가 저장되면 처리할 코드
                    editText.text.clear() // 메시지 입력란 비우기
                }
                .addOnFailureListener { e ->
                    // 메시지 저장 실패 시 처리할 코드
                    println("Error sending message: $e")
                }
        }
    }
}
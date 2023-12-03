package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ItemShowActivity: AppCompatActivity(){

    private lateinit var firestore: FirebaseFirestore
    private lateinit var postId: String // 게시물의 고유 ID

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_show)

        postId = intent.getStringExtra("postid") ?: ""
        firestore = FirebaseFirestore.getInstance()
        val documentReference = firestore.collection("postdb").document(postId)
        val sellertext = findViewById<TextView>(R.id.textSeller)
        val titletext = findViewById<TextView>(R.id.textTitle)
        val contenttext = findViewById<TextView>(R.id.textContent)
        val salsesstatustext = findViewById<TextView>(R.id.textSaleStatus)
        val pricetext = findViewById<TextView>(R.id.textPrice)
        val sendmsgbutton = findViewById<Button>(R.id.btnSendMessage)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // 문서가 존재하는 경우 데이터 추출
                    val title = documentSnapshot.getString("title") ?: ""
                    val email = documentSnapshot.getString("email") ?: ""
                    val price = documentSnapshot.getLong("price")?.toInt() ?: 0
                    val sales_status = documentSnapshot.getBoolean("salesstatus") ?: false
                    val content = documentSnapshot.getString("content") ?: ""
                    // 여기에서 가져온 데이터를 활용하여 작업 수행
                    titletext.text = title
                    sellertext.text = email
                    contenttext.text= content
                    if(sales_status==true)
                        salsesstatustext.text = "판매 완료"
                    else
                        salsesstatustext.text = "판매중"
                    pricetext.text= price.toString()

                    // 예: TextView 등에 데이터를 표시
                } else {

                }
            }
            .addOnFailureListener { e ->
                // 문서 가져오기 실패 시 처리
                // 예: 에러 로그 출력
            }
       sendmsgbutton.setOnClickListener {
           val intent = Intent(this, ChatActivity::class.java)
           // 필요한 경우 인텐트에 데이터를 추가할 수 있습니다.
           intent.putExtra("postid",postId)
           //게시글 수정
           startActivity(intent)

       }
    }
}
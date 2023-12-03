package com.example.finalproject

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ItemFixActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postId: String // 게시물의 고유 ID
    private var previousPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fixed_item)
        postId = intent.getStringExtra("postid") ?: ""

        firestore = FirebaseFirestore.getInstance()

        val documentReference = firestore.collection("postdb").document(postId)
        val titletext = findViewById<TextView>(R.id.textViewTitle)
        val savebutton = findViewById<Button>(R.id.buttonSave)
        val editTextPrice = findViewById<EditText>(R.id.editTextPrice)
        val checkBoxSaleStatus = findViewById<CheckBox>(R.id.checkBoxSaleStatus)
// 문서 가져오기
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // 문서가 존재하는 경우 데이터 추출
                    val title = documentSnapshot.getString("title")
                    // 여기에서 가져온 데이터를 활용하여 작업 수행
                    titletext.text = title
                    previousPrice = documentSnapshot.getLong("price")?.toInt() ?: 0
                    // EditText에 이전 가격을 설정
                    editTextPrice.setText(previousPrice.toString())
                    // 예: TextView 등에 데이터를 표시
                } else {

                }
            }
            .addOnFailureListener { e ->
                // 문서 가져오기 실패 시 처리
                // 예: 에러 로그 출력
            }
        savebutton.setOnClickListener{

            val priceString = editTextPrice.text.toString()
            val price: Int = try {
                if (priceString.isNotBlank()) {
                    priceString.toInt()
                } else {
                    previousPrice
                    // 빈 칸인 경우 기존 가격을 그대로 유지
                    // 여기에서 이전에 가져온 가격을 사용하거나, 따로 저장된 가격 변수를 사용할 수 있습니다.
                    // 예: 이전에 가져온 가격이 저장된 변수가 있다면 그 변수를 사용
                    // 예: 이전에 화면에 표시된 가격이 있다면 그 값을 사용
                    // 이 코드에서는 일단 0으로 설정하고, 필요에 따라 수정하십시오.

                }
            } catch (e: NumberFormatException) {
                // 숫자로 변환할 수 없는 경우 0으로 설정
                previousPrice
            }

            // 판매 여부 확인
            val salesStatus = checkBoxSaleStatus.isChecked

            // 수정된 데이터를 파이어스토어에 저장
            updatePostData(price, salesStatus)
        }


    }
    private fun updatePostData(price: Int, salesStatus: Boolean) {
        // 수정된 데이터를 맵 형태로 생성
        val postMap: Map<String, Any> = hashMapOf(
            "price" to price,
            "salesstatus" to salesStatus
        )

        // 파이어스토어에서 해당 게시물의 문서를 찾아 수정된 데이터를 업데이트
        firestore.collection("postdb").document(postId)
            .update(postMap)
            .addOnSuccessListener {
                // 성공적으로 업데이트되면 실행할 코드
                println("DocumentSnapshot successfully updated!")
                finish() // 액티비티 종료
            }
            .addOnFailureListener { e ->
                // 실패 시 실행할 코드 (에러 로그 출력)
                println("Error updating document: $e")
            }
    }

}
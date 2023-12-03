package com.example.finalproject.chat


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.HomeItem
import com.example.finalproject.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.sql.Timestamp
import com.example.finalproject.R
import com.example.finalproject.home.HomeAdapter

class ChatFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var messageAdapter: MessageAdapter
    val itemListLiveData = MutableLiveData<List<Message>>()
    private  lateinit var  RecyclerView: RecyclerView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        RecyclerView = view.findViewById<RecyclerView>(R.id.chatListRecyclerView)

        return view
    }
    private fun initializeRecyclerView() {
        RecyclerView.layoutManager = LinearLayoutManager(activity)
        messageAdapter = MessageAdapter(itemListLiveData.value ?: emptyList())
        RecyclerView.adapter = messageAdapter
    }
    override fun onStart() {
        super.onStart()
         initializeRecyclerView()
        getmessageList()
        itemListLiveData.observe(viewLifecycleOwner, Observer {
            messageAdapter.addMessages(it)
        })
    }

    private fun getmessageList()    {
        val messages = mutableListOf<Message>()
        val collectionReference = firestore.collection("messages")
        val receiverUserId = auth.currentUser?.email
        // 최신 메시지가 맨 아래로 표시되도록 스크롤 이동
        collectionReference
            .whereEqualTo("receiverName", receiverUserId)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val sender = document.getString("senderEmail") ?: ""
                    val text = document.getString("message") ?: ""

                    val message = Message(sender, text)
                    messages.add(message)
                }

                Log.d("ChatFragment", "메시지 수: ${messages.size}")

                messageAdapter.addMessages(messages)

            }
        itemListLiveData.value = messages // LiveData 업데이트
    }

}
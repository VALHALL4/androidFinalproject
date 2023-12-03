package com.example.finalproject.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.finalproject.HomeItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.ItemAddActivity
import com.example.finalproject.ItemFixActivity
import com.example.finalproject.ItemShowActivity
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() ,HomeAdapter.OnItemClickListener{


    private lateinit var view: View
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val itemListLiveData = MutableLiveData<List<HomeItem>>()
    private  lateinit var  RecyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_home, container, false)
      RecyclerView = view.findViewById(R.id.homeRecyclerView)

        floatingActionButton = view.findViewById(R.id.addFloatingButton)
        floatingActionButton.setOnClickListener {
            //게시글 작성 액티비티
            val intent = Intent(activity, ItemAddActivity::class.java)
            startActivity(intent)
        }

        val spinnerFilter = view.findViewById<Spinner>(R.id.spinnerFilter)
        val filterOptions = arrayOf("전체", "판매 중", "판매 완료")

// ArrayAdapter를 사용하여 Spinner에 데이터 설정
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = adapter

// Spinner의 선택 이벤트 처리
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                selectedItemView?.let {
                    val selectedOption = filterOptions[position]
                    // 필터링된 데이터를 RecyclerView에 표시하도록 처리
                    val filteredItemList = filterItemList(selectedOption)

                    // RecyclerView 초기화 및 필터링된 데이터 설정
                    initializeRecyclerView(filteredItemList)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // 아무 것도 선택되지 않았을 때의 처리
                initializeRecyclerView(filterItemList("전체"))
            }
        }

        return view
    }
    private fun initializeRecyclerView(filteredItemList: List<HomeItem>) {
        RecyclerView.layoutManager = LinearLayoutManager(activity)
        homeAdapter = HomeAdapter(filteredItemList, this)
        RecyclerView.adapter = homeAdapter
    }


    override fun onItemClick(item: HomeItem) {
        // 여기서 다른 액티비티로 이동하는 코드를 작성
        val email = item.email
        val currentemail = currentUser?.email

        if(email == currentemail){
            val intent = Intent(activity, ItemFixActivity::class.java)
            // 필요한 경우 인텐트에 데이터를 추가할 수 있습니다.
            println("수정")
            intent.putExtra("postid",item.postId)
            //게시글 수정
            startActivity(intent)
        }
        else{
            val intent = Intent(activity, ItemShowActivity::class.java)
            // 필요한 경우 인텐트에 데이터를 추가할 수 있습니다.
            intent.putExtra("postid",item.postId)
            //게시글 자세히보기
            println("보기")
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        initializeRecyclerView(filterItemList("전체"))
        // LiveData를 관찰하여 데이터가 변경될 때마다 RecyclerView를 갱신
        getSampleItemList()
        itemListLiveData.observe(viewLifecycleOwner, Observer {
            homeAdapter.updateItemList(it)
        })
    }

    private fun filterItemList(filterOption: String): List<HomeItem> {
        val originalItemList = itemListLiveData.value ?: emptyList()

        return when (filterOption) {
            "전체" -> originalItemList // 아무런 필터링 없이 전체 데이터 반환
            "판매 중" -> originalItemList.filter { !it.sales_status }
            "판매 완료" -> originalItemList.filter { it.sales_status }
            else -> emptyList() // 기본적으로 빈 리스트 반환
        }
    }


    private fun getSampleItemList() {
        val itemList = mutableListOf<HomeItem>()

        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("postdb")

        collectionReference.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val username = document.getString("username") ?: ""
                    val price = document.getLong("price")?.toInt() ?: 0
                    val sales_status = document.getBoolean("salesstatus") ?: false
                    val email = document.getString("email") ?: ""
                    val postid= document.id


                    val homeItem = HomeItem(title, username, price, sales_status,email,postid)
                    itemList.add(homeItem)
                }

                itemListLiveData.value = itemList // LiveData 업데이트
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }
    }



}
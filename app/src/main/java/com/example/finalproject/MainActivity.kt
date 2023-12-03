package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.finalproject.chat.ChatFragment
import com.example.finalproject.home.HomeFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

// androidx에 있는 Fragment를 import 해와야 함


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatFragment = ChatFragment()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // fragment 초기값
        replaceFragment(homeFragment)

        // setOnNavigationItemSelectedListener는 네비게이션바의 탭들이 선택되었을 때 호출되어 선택된 탭의 id가 내려온다.
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatFragment)
                R.id.mypage ->  startActivity(Intent(this, MyPageActivity::class.java))

            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    // FrameLayout에 선택된 Fragment를 attach하는 메소드
    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}


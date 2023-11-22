package com.example.meal_time

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.meal_time.auth.IntroActivity
import com.example.meal_time.auth.LoginActivity
import com.example.meal_time.databinding.ActivityMainBinding
import com.example.meal_time.setting.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        //로그아웃 버튼 눌렀을때 이벤트 처리
        /*binding.logoutBtn.setOnClickListener {
            //로그아웃 기능
            auth.signOut()
            //로그인, 회원가입때랑 똑같이 뒤로가기 버튼을 눌렀을때 앱이 종료되게 동일하게 구현
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }*/
    }
}


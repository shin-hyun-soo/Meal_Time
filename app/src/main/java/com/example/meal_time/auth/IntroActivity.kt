package com.example.meal_time.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

//레이아웃 파일과 결합할 수 있도록 DataBindingUtil 사용하여 결합시켜줌
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

/*Activity 간 전환을 위해 intent 사용, 로그인 버튼을 눌렀을때 LoginActivity로 이동
 join 버튼을 눌렀을때 JoinActivity로 이동, startActivity(intent)는 화면간 전환을 담당
 */

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }
    }
}
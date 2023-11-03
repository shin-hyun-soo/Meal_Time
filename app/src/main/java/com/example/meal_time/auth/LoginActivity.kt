package com.example.meal_time.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.meal_time.MainActivity
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_login) 필요없는 부분이라 주석 처리

        //동일하게 초기화 파이어베이스 초기화 시켜줌
        auth = Firebase.auth


        //레이아웃 파일과 결합을 위해 databindingutil 사용
       binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
       //로그인 버튼 클릭시 이벤트 작성
       binding.loginBtn.setOnClickListener {

           val email = binding.emailEnter.text.toString()
           val password = binding.passwordEnter.text.toString()

           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {

                       val intent = Intent(this, MainActivity::class.java)
                       //Activity가 실행되었을 때 뒤로 가기 버튼을 누르면 이전 페이지로 돌아가지 않고 어플이 종료되도록 설정
                       intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                       startActivity(intent)

                       Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()

                   } else {

                       Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()

                   }
               }
       }

    }
}
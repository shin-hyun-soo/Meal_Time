package com.example.meal_time.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.meal_time.MainActivity
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    //FirebaseAuth의 인스턴스 선언
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //onCreate에서 FirebaseAuth 인스턴스 초기화
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join )

        binding.joinBtn.setOnClickListener {

            var check = true

            val email = binding.emailEnter.text.toString()
            val password1 = binding.passwordEnter.text.toString()
            val password2 = binding.passwordEnter2.text.toString()

            //email, password, password check 값이 비어있는지 확인 후 아닌경우 false
            if(email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                check = false
            }
            if(password1.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
                check = false
            }
            if(password2.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
                check = false
            }
            //password와 password check 값이 똑같은지 확인 후 아닌경우 false
            if(!password1.equals(password2)) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_LONG).show()
                check = false
            }
            //비밀번호가 6자리 이상인지 확인 아닌경우 false
            if(password1.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요", Toast.LENGTH_LONG).show()
                check = false
            }
            if(check) {
                auth.createUserWithEmailAndPassword(email,password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, MainActivity::class.java)
                            //Activity가 실행되었을 때 뒤로 가기 버튼을 누르면 이전 회원가입 페이지로 돌아가지 않고 어플이 종료되도록 설정
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "실패", Toast.LENGTH_LONG).show() }
                    }
                }
            }
        }



}
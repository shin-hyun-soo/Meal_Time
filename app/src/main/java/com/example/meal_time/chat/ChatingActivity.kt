package com.example.meal_time.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityChatingBinding
//사용자리스트를 만들어야하고 리스트를 만드는 코드 짜ㅑ한다
//그럴려면 recipefragment에 snapshot으로 지금까지 있는 유저 아이디 다 가져와서 사용자리스트에 넣고 auth의 사용자 아이디(지금 사용자)와 database의 아이디(다른 회원가입됨 사용자)
//가 다르면 리스트에 넣으면 지금 사용자가 아닌 사용자들의 유아이디를 저장!!
class ChatingActivity : AppCompatActivity() {

    private lateinit var receiverUid: String

    private lateinit var binding: ActivityChatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chating)

        //넘어온 데이터 변수에 담기
        receiverUid=intent.getStringExtra("uId").toString()
        supportActionBar?.title="익명"

        //supportActionBar?.title=receiverName
    }
}
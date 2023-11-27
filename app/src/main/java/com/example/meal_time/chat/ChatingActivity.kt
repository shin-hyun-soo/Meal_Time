package com.example.meal_time.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityChatingBinding

class ChatingActivity : AppCompatActivity() {

    private lateinit var key: String

    private lateinit var binding: ActivityChatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chating)

        //Intent로부터 채팅방의 고유 키(key) 값 받앋오기
        key=intent.getStringExtra("key").toString()

        //supportActionBar?.title=receiverName
    }
}
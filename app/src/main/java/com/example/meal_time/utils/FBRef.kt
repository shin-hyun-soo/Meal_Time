package com.example.meal_time.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database



class FBRef {

    companion object{

        private val database = Firebase.database

            //파이어베이스의 보드에 대한 정보
        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")
        //서진 오류나면 지울것
        val chatRef = database.getReference("chat")

    }
}
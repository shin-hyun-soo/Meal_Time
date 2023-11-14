package com.example.meal_time.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database



class FBRef {

    companion object{

        private val database = Firebase.database

        val boardRef = database.getReference("board")

    }
}
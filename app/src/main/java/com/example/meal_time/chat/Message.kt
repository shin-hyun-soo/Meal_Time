package com.example.meal_time.chat

data class Message(
    var message: String?,
    var sendId: String?,
    var time: String?
){
    constructor():this("","","")
}


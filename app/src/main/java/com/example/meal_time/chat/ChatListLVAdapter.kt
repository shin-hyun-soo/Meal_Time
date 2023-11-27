package com.example.meal_time.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.meal_time.R

class ChatListLVAdapter(val chatList: MutableList<ChatModel>) : BaseAdapter() {
    //리스트뷰에 표시할 아이템의 개수 반환
    override fun getCount(): Int{
        return chatList.size
    }

    //특정 위치의 아이템 가져오기
    override fun getItem(position: Int): Any {
        return chatList[position]
    }
    //아이템의 ID 반환
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        //재활용 가능한 뷰가 없을 경우 새로운 뷰 생성
        view=LayoutInflater.from(parent?.context).inflate(R.layout.chat_list_item,parent,false)
        val itemLinearLayoutView=view?.findViewById<LinearLayout>(R.id.chatitemView)
        val time = view?.findViewById<TextView>(R.id.chattimeArea)
        itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        time!!.text = chatList[position].time
        return view!!
    }


}
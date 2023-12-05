package com.example.meal_time.chat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meal_time.R


class ChatListLVAdapter(val chatList: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatListLVAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val currentUser=chatList[position]
        holder.nameText.text="익명"

        holder.itemView.setOnClickListener{
            //it이 확실하지 않음
            val intent= Intent(holder.itemView.context,ChatingActivity::class.java)
            intent.putExtra("uId",currentUser)
            holder.itemView.context.startActivity(intent)
        }
    }
    //리스트뷰에 표시할 아이템의 개수 반환
    override fun getItemCount(): Int{
        return chatList.size
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameText: TextView=itemView.findViewById(R.id.name_text)
    }

}
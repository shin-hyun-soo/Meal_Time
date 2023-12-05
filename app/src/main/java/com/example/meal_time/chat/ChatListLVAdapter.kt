package com.example.meal_time.chat

import android.app.AlertDialog
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
import androidx.room.Delete
import com.example.meal_time.R
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.TreeMap


class ChatListLVAdapter(val chatList: ArrayList<ChatModel>) :

    RecyclerView.Adapter<ChatListLVAdapter.ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.nameText.text="익명 채팅방"
        val currentUser=chatList[position]
        //채팅방 클릭
        holder.itemView.setOnClickListener{
            val intent= Intent(holder.itemView.context,ChatingActivity::class.java)
            intent.putExtra("uId",currentUser.uid)
            holder.itemView.context.startActivity(intent)
        }
        //채팅방 삭제
        holder.itemView.setOnLongClickListener{
            showDeleteConfirmationDialog(holder.itemView.context,currentUser)
            true
        }

    }

    private fun showDeleteConfirmationDialog(context: Context,chatModel: ChatModel){
        AlertDialog.Builder(context)
            .setTitle("채팅방 삭제")
            .setMessage("정말로 이 채팅방을 삭제하시겠습니까?")
            .setPositiveButton("삭제"){ _, _ ->
                //채팅방 삭제 로직 실행
                deleteChatRoom(chatModel)
            }
            .setNegativeButton("취소",null)
            .show()
    }

    private fun deleteChatRoom(chatModel: ChatModel){
        FBRef.chatRef.child(chatModel.uid).removeValue()
        val position=chatList.indexOf(chatModel)
        if (position != -1){
            chatList.removeAt(position)
            notifyItemRemoved(position)
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
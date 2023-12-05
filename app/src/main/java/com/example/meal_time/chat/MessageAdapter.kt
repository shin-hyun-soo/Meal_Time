package com.example.meal_time.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meal_time.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MessageAdapter(private val context: Context,private val  messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val receive=1 //받는 타입
    private val send=2 //보내는 타입

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==1){
            val view: View =LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            ReceiveViewHolder(view)
        }else{
            val view:View=LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.textView_time.text=currentMessage.time
            viewHolder.sendMessage.text = currentMessage.message
            viewHolder.sendMessage.setBackgroundResource(R.drawable.orangechat)


        } else {
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.textView_time.text=currentMessage.time
            viewHolder.receiveMessage.text = currentMessage.message
            viewHolder.receiveMessage.setBackgroundResource(R.drawable.blackchat)

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)) {
            send
        } else {
            receive
        }
    }

    class SendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sendMessage: TextView=itemView.findViewById(R.id.send_message_text)
        val textView_time : TextView=itemView.findViewById(R.id.message_time)
    }
    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message_text)
        val textView_time : TextView=itemView.findViewById(R.id.message_time)
    }
}
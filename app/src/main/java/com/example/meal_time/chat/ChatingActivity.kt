package com.example.meal_time.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityChatingBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.chat_list_item.*


class ChatingActivity : AppCompatActivity() {

    private lateinit var receiverUid: String

    private lateinit var binding: ActivityChatingBinding

    lateinit var auth: FirebaseAuth
    lateinit var fbref: DatabaseReference

    private lateinit var receiverRoom: String
    private lateinit var senderRoom: String

    private lateinit var messageList: ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chating)

        //초기화
        messageList= ArrayList()
        val messageAdapter:MessageAdapter= MessageAdapter(this,messageList)

        //recyclerVIew
        binding.chatRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.chatRecyclerView.adapter=messageAdapter

        //넘어온 데이터 변수에 담기
        receiverUid=intent.getStringExtra("uId").toString()
        auth= FirebaseAuth.getInstance()
        fbref=FirebaseDatabase.getInstance().reference

        //접속자 uId
        val senderUid=auth.currentUser?.uid

        //보낸이방
        senderRoom=receiverUid+senderUid
        //받는이방
        receiverRoom=senderUid+receiverUid


        val time = FBAuth.getTime()

        binding.sentBtn.setOnClickListener{
            val message=binding.messageEdit.text.toString()
            val messageObject=Message(message, senderUid,time)

            //데이터 저장
            fbref.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //저장 성공하면
                    fbref.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }

            binding.messageEdit.setText("")
        }

        //메세지 가져오기
        fbref.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    //적용
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
}
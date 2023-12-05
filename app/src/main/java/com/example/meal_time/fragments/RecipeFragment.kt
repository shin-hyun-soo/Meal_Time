package com.example.meal_time.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meal_time.R
import com.example.meal_time.chat.ChatListLVAdapter
import com.example.meal_time.chat.ChatModel
import com.example.meal_time.chat.ChatingActivity
import com.example.meal_time.chat.Message
import com.example.meal_time.databinding.FragmentRecipeBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

//서진
class RecipeFragment : Fragment() {

    private lateinit var binding : FragmentRecipeBinding

    //채팅방 데이터와 키 저장할 리스트 초기화
    private lateinit var chatList: ArrayList<ChatModel>
    lateinit var messageList: ArrayList<Message>

    //채팅방 목록을 보여주는 어댑터
    private lateinit var  adapter: ChatListLVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_recipe,container,false)
        chatList=ArrayList()
        adapter=ChatListLVAdapter(chatList)

        binding.userRecyclerView.layoutManager=LinearLayoutManager(getActivity())
        binding.userRecyclerView.adapter=adapter

        //사용자 정보 가져오기(자기가 아닌 사용자의 uid를 chat list에 저장 이후 이 리스트를 가지고 방을 만듬
        FBRef.chatRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    //유저 정보
                    val currentUser=postSnapshot.getValue(ChatModel::class.java)
                    if(FBAuth.getUid()!=currentUser?.uid){
                        chatList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //실패시 실행
            }
        })


        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_recipeFragment_to_homeFragment)
        }
        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_recipeFragment_to_talkFragment)
        }

        //getFBChatData()
        return binding.root
    }

}
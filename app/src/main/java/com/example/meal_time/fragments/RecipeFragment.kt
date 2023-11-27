package com.example.meal_time.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.meal_time.R
import com.example.meal_time.chat.ChatListLVAdapter
import com.example.meal_time.chat.ChatModel
import com.example.meal_time.chat.ChatingActivity
import com.example.meal_time.databinding.FragmentRecipeBinding
import com.example.meal_time.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//서진
class RecipeFragment : Fragment() {

    private lateinit var binding : FragmentRecipeBinding

    //채팅방 데이터와 키 저장할 리스트 초기화
    private val chatDataList = mutableListOf<ChatModel>()
    private val chatKeyList = mutableListOf<String>()

    //채팅방 목록을 보여주는 어댑터
    private lateinit var chatRVAdapter: ChatListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_recipe,container,false)

        // 어댑터 초기화 후 ListView에 연결
        chatRVAdapter= ChatListLVAdapter(chatDataList)
        binding.chatListView.adapter=chatRVAdapter

        binding.chatListView.setOnItemClickListener{ parent, view, position, id ->
            //chatingActivity로 이동하면서 해당 게시물 데이터 전달
            val intent= Intent(context, ChatingActivity::class.java)
            intent.putExtra("key",chatKeyList[position])
            startActivity(intent)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_recipeFragment_to_homeFragment)
        }
        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_recipeFragment_to_talkFragment)
        }
        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData(){
        val postListener = object: ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                chatDataList.clear()

                for (dataModel in datasnapshot.children){
                    val item=dataModel.getValue(ChatModel::class.java)
                    chatDataList.add(item!!)
                    chatKeyList.add(dataModel.key.toString())
                }
                chatDataList.reverse()
                chatDataList.reverse()
                chatRVAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        FBRef.chatRef.addValueEventListener(postListener)
    }
/*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecipeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    */
}
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
import com.example.meal_time.databinding.FragmentRecipeBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//서진
class RecipeFragment : Fragment() {

    private lateinit var binding : FragmentRecipeBinding

    //채팅방 데이터와 키 저장할 리스트 초기화
    private lateinit var chatList: ArrayList<ChatModel>
    //private val chatKeyList = mutableListOf<String>()

    //채팅방 목록을 보여주는 어댑터
    private lateinit var  adapter: ChatListLVAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        //auth에서 uid랑 time가져오기
        val uid = FBAuth.getUid()
        val time = FBAuth.getTime()

        //board
        //  -key
        //    -boardModel(title, content, uid, time)
        /*게시글을 클릭했을때, 게시글에 대한 정보를 받아와야함, 근데 이미지 이름에 대한
        정보를 모르기 때문에 이미지 이름을 문서의 key 갑승로 해서 이미지에 대한 정보를 찾기
        쉽게 해놓음
         */
        val key = FBRef.chatRef.push().key.toString()

        //database에 저장!!!!
        FBRef.chatRef
            .child(key)
            .setValue(ChatModel(uid))

 */
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

        //사용자 정보 가져오기
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
        // 어댑터 초기화 후 ListView에 연결
        /*
        chatRVAdapter= ChatListLVAdapter(chatDataList)
        binding.chatListView.adapter=chatRVAdapter

        binding.chatListView.setOnItemClickListener{ parent, view, position, id ->
            //chatingActivity로 이동하면서 해당 게시물 데이터 전달
            val intent= Intent(context, ChatingActivity::class.java)
            intent.putExtra("key",chatKeyList[position])
            startActivity(intent)
        }
        */
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_recipeFragment_to_homeFragment)
        }
        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_recipeFragment_to_talkFragment)
        }

        //getFBChatData()
        return binding.root
    }
/*
    private fun getFBChatData(){
        auth= FirebaseAuth.getInstance()
        auth.("user").addValueEventListener(object: ValueEventListener{
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
*/
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
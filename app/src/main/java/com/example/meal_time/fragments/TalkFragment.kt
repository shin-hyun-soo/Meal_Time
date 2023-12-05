package com.example.meal_time.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meal_time.board.BoardInsideActivity
import com.example.meal_time.board.BoardListLVAdapter
import com.example.meal_time.board.BoardModel
import com.example.meal_time.board.BoardWriteActivity
import com.example.meal_time.board.viewmodel.BoardViewModel
import com.example.meal_time.utils.FBRef
import com.example.meal_time.R
import com.example.meal_time.databinding.FragmentTalkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class TalkFragment : Fragment() {

    private lateinit var binding: FragmentTalkBinding

    private val TAG = TalkFragment::class.java.simpleName
    private val viewModel by viewModels<BoardViewModel>()

    private lateinit var boardRVAdapter: BoardListLVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        lifecycleScope.launchWhenCreated {
            viewModel.boardList.collect { boardList ->
                Log.d(TAG, "boardList: $boardList")
                boardRVAdapter = BoardListLVAdapter(boardList)
                binding.boardListView.adapter = boardRVAdapter
            }
        }


        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            //listview에 있는 데이터를 다 다른 액티비티로 전달해서 만들기
            /*val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("title", boardDataList[position]. title)
            intent.putExtra("content", boardDataList[position].content)
            intent.putExtra("time", boardDataList[position].time)
            startActivity(intent)*/

            //Firebase에 있는 board에 대한 데이터 id 기반으로 데이터 받는 방법법
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", viewModel.keyList.value[position])
            startActivity(intent)
        }
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        //서진
        binding.recipeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_recipeFragment)
        }


        return binding.root
    }


}
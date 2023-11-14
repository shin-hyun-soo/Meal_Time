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
import com.example.meal_time.board.BoardListLVAdapter
import com.example.meal_time.board.BoardModel
import com.example.meal_time.board.BoardWriteActivity
import com.example.meal_time.databinding.FragmentTalkBinding


class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        val boardList = mutableListOf<BoardModel>()
        boardList.add(BoardModel("a", "b", "c", "d"))

        val boardRVAdapter = BoardListLVAdapter(boardList)
        binding.boardListView.adapter = boardRVAdapter

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        return binding.root
    }


}
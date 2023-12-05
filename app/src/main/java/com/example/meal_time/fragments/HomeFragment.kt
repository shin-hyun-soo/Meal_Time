package com.example.meal_time.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meal_time.R
import com.example.meal_time.databinding.FragmentHomeBinding
import com.example.meal_time.todolist.Todo
import com.example.meal_time.todolist.TodoAdapter


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private lateinit var todoAdapter : TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        // Null check for RecyclerView
        val rvTodoItems: RecyclerView = binding.rvTodoItems
        // RecyclerView에 레이아웃 매니저를 설정합니다.
        rvTodoItems.layoutManager = LinearLayoutManager(context)
        // TodoAdapter를 초기화하고 RecyclerView에 설정합니다.
        todoAdapter = TodoAdapter(requireContext(), mutableListOf())
        rvTodoItems.adapter = todoAdapter

        // "추가" 버튼에 클릭 리스너를 등록합니다.
        binding.btnAddTodo2.setOnClickListener {
//            // 입력된 할 일의 제목을 가져옵니다.
//            val todoTitle = etTodoTitle.text?.toString() ?: ""
//
//            // 제목이 비어있지 않다면 새로운 Todo 객체를 생성하고 어댑터에 추가합니다.
//            if (todoTitle.isNotEmpty()) {
//                val todo = Todo(todoTitle)
//                todoAdapter.addTodo(todo)
//                // 입력 필드를 초기화합니다.
//                etTodoTitle.text.clear()
//            }
        }
        // "완료된 할 일 삭제" 버튼에 클릭 리스너를 등록합니다.
        binding.btnDeleteDoneTodo2.setOnClickListener {
            // 어댑터에서 완료된 할 일을 삭제하는 함수를 호출합니다.
            todoAdapter.deleteDoneTodos()
        }


        //서진
        binding.talkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)
        }
        binding.recipeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_recipeFragment)
        }

        return binding.root
    }


}
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
import kotlinx.android.synthetic.main.fragment_home.*


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
        rvTodoItems.layoutManager = LinearLayoutManager(context)
        todoAdapter = TodoAdapter(mutableListOf())
        rvTodoItems.adapter = todoAdapter
        // Null check for EditText
        //val etTodoTitle1: EditText = binding.etTodoTitle1


        binding.btnAddTodo2.setOnClickListener {
            val todoTitle = etTodoTitle.text?.toString() ?: ""
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }

        binding.btnDeleteDoneTodo2.setOnClickListener {
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
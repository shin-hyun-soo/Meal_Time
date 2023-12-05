package com.example.meal_time.todolist

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meal_time.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(
    private val context: Context,
    private var todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    // Todo 항목의 뷰홀더 클래스 정의
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // 뷰홀더를 생성하고 반환하는 함수
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    // 텍스트에 취소선을 토글하는 함수
    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    // 새로운 할 일을 추가하는 함수
    fun addTodo(todo: Todo) {
        todos.add(todo)
        saveTodos()
        notifyItemInserted(todos.size -1)
    }
    // 완료된 할 일을 삭제하는 함수
    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        saveTodos()
        notifyDataSetChanged()
    }

    private fun loadTodos() {
        val sharedPreferences = context.getSharedPreferences("todos", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("todos", "")
        val type = object : TypeToken<MutableList<Todo>>() {}.type
        todos = gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun saveTodos() {
        val sharedPreferences = context.getSharedPreferences("todos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(todos)
        editor.putString("todos", json)
        editor.apply()
    }
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        // 각 뷰홀더에 데이터를 바인딩하는 함수
        val curTodo = todos[position]
        holder.itemView.apply {

            // 뷰홀더의 뷰들을 현재 할 일의 데이터로 설정
//            tvTodoTitle.text = curTodo.title
//            cbDone.isChecked = curTodo.isChecked
//
//            // 완료 체크박스의 체크 상태에 따라 텍스트의 취소선을 토글하고 데이터 업데이트
//            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
//            cbDone.setOnCheckedChangeListener { _, isChecked ->
//                toggleStrikeThrough(tvTodoTitle, isChecked)
//                curTodo.isChecked = !curTodo.isChecked
//            }
        }
    }
    init {
        // Load todos when the adapter is created
        loadTodos()
    }
    // 아이템 개수 반환
    override fun getItemCount(): Int {
        return todos.size
    }
}
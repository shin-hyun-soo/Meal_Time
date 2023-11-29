package com.example.meal_time

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meal_time.databinding.ActivityMainBinding
import com.example.meal_time.diary.DiaryActivity
import com.example.meal_time.setting.SettingActivity
import com.example.meal_time.timer.TimerActivity
import com.example.meal_time.todolist.Todo
import com.example.meal_time.todolist.TodoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    private lateinit var todoAdapter : TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)

        todoAdapter = TodoAdapter(mutableListOf())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        /*rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)


        binding.btnAddTodo2.setOnClickListener {
            val todoTitle = etTodoTitle1.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle1.text.clear()
            }
        }

        binding.btnDeleteDoneTodo2.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }

         */

        binding.timerbtn.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }
        binding.diarybtn.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }

        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        //로그아웃 버튼 눌렀을때 이벤트 처리
        /*binding.logoutBtn.setOnClickListener {
            //로그아웃 기능
            auth.signOut()
            //로그인, 회원가입때랑 똑같이 뒤로가기 버튼을 눌렀을때 앱이 종료되게 동일하게 구현
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }*/
    }
}


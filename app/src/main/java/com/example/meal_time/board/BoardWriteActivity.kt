package com.example.meal_time.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityBoardWriteBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef


class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()



            //board
            //  -key
            //    -boardModel(title, content, uid, time)

            FBRef.boardRef
                .push()
                .setValue(BoardModel(title, content, uid, time))

            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

            finish()
        }




    }
}
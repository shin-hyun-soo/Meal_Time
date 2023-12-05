package com.example.meal_time.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.meal_time.board.viewmodel.BoardViewModel
import com.example.meal_time.comment.CommentModel
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.R
import com.example.meal_time.comment.CommentLVAdapter
import com.example.meal_time.databinding.ActivityBoardInsideBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardInsideBinding
    private val viewModel by viewModels<BoardViewModel>()

    private lateinit var key: String
    private lateinit var commentAdapter: CommentLVAdapter //댓글

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSetIcon.setOnClickListener {
            showDialog()
        }

        //두번째 방법
        //firebase 키 값에 대한 정보(키값을 이용한 삭제 가능)
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageDate(key)
        getCommentData(key)

        binding.commentBtn.setOnClickListener {
            insertCommnet(key)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.commentList.collect { commentList ->
                Log.d(TAG, "commentList: $commentList")
                commentAdapter = CommentLVAdapter(commentList as MutableList<CommentModel>)
                binding.commentLV.adapter = commentAdapter
            }
        }


    }

    private fun getCommentData(key: String) {
        viewModel.getComment(key)
    }

    private fun insertCommnet(key: String) {
        viewModel.addComment(
            key, CommentModel(
                binding.commentArea.text.toString(),
                FBAuth.getTime()
            )
        )


        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        //댓글 입력하면 사라지게
        binding.commentArea.setText("")

    }

    //다이얼로그 띄우는 부분
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        //수정 삭제 부분
        //수정
        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            // dialog 사라지게
            alertDialog.dismiss()
            val intent = Intent(this, BoardEditActivity::class.java)
            //수정을 위해 key값도 같이 넘겨줌줌
            intent.putExtra("key", key)
            startActivity(intent)
        }
        //삭제
        alertDialog.findViewById<Button>(R.id.deleteBtn)?.setOnClickListener {
            alertDialog.dismiss()
            viewModel.deleteBoardData(key)
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getImageDate(key: String) {
        lifecycleScope.launch {
            val image = async { viewModel.getImageData(key) }.await()
            if (image == null) {
                binding.getImageArea.isVisible = false

            } else {
                Glide.with(this@BoardInsideActivity)
                    .load(image)
                    .into(binding.getImageArea)
            }
        }
    }

    private fun getBoardData(key: String) {
        lifecycleScope.launch {
            val dataModel = async { viewModel.getBoardData(key) }.await()
            if (dataModel == null) {
                Toast.makeText(this@BoardInsideActivity, "삭제된 게시글입니다", Toast.LENGTH_LONG).show()
                finish()
                return@launch
            }
            binding.titleArea.text = dataModel.title
            binding.textArea.text = dataModel.content
            binding.timeArea.text = dataModel.time

            val myUid = FBAuth.getUid()
            val writerUid = dataModel.uid

            if (myUid.equals(writerUid)) {
                Log.d(TAG, "내가 쓴 글")
                binding.boardSetIcon.isVisible = true
            } else {
                Log.d(TAG, "내가 쓴 글 아님")
            }

        }
    }

}
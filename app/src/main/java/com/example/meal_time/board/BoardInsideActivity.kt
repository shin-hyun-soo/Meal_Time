package com.example.meal_time.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.meal_time.R
import com.example.meal_time.comment.CommentLVAdapter
import com.example.meal_time.comment.CommentModel
import com.example.meal_time.databinding.ActivityBoardInsideBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardInsideBinding

    private lateinit var key: String

    private val commentDataList = mutableListOf<CommentModel>() //댓글

    private lateinit var commentAdapter : CommentLVAdapter //댓글

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
        //첫번째 방법
        /*val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()
        val time = intent.getStringExtra("time").toString()

        binding.titleArea.text = title
        binding.textArea.text = content
        binding.timeArea.text = time*/

        binding.boardSetIcon.setOnClickListener {
            showDialog()
        }

        //두번째 방법
        //firebase 키 값에 대한 정보(키값을 이용한 삭제 가능)
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageDate(key)

        binding.commentBtn.setOnClickListener {
            insertCommnet(key)
        }

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter

        getCommentData(key)
    }

    fun getCommentData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    fun insertCommnet(key: String) {
        //comment
        // -Boardkey
        //      - CommentKey(자동생성)
        //          -CommentData
        //          -CommentData
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(binding.commentArea.text.toString(),
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

            val intent = Intent(this, BoardEditActivity::class.java)

            //수정을 위해 key값도 같이 넘겨줌줌
           intent.putExtra("key", key)
            startActivity(intent)
        }
        //삭제
        alertDialog.findViewById<Button>(R.id.deleteBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getImageDate(key: String) {
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener({ task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.getImageArea.isVisible = false
            }
        })


    }

    private fun getBoardData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    //uid 값 받아와서 firebase uid = writer uid 값이 같으면 seticon 보여주기
                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if (myUid.equals(writerUid)){
                       Log.d(TAG, "내가 쓴 글")
                        binding.boardSetIcon.isVisible = true
                    } else {
                        Log.d(TAG, "내가 쓴 글 아님")
                    }
                    //에러가 발생하면 CATCH로 빠지기(예외처리)
                }catch (e : Exception){
                    Log.d(TAG, "삭제완료")
                    }

                }




            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }


}
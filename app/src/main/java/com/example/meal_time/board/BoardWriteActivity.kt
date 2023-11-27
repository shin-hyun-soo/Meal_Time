package com.example.meal_time.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityBoardWriteBinding
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)



            //board
            //  -key
            //    -boardModel(title, content, uid, time)
            /*게시글을 클릭했을때, 게시글에 대한 정보를 받아와야함, 근데 이미지 이름에 대한
            정보를 모르기 때문에 이미지 이름을 문서의 key 갑승로 해서 이미지에 대한 정보를 찾기
            쉽게 해놓음
             */
            val key = FBRef.boardRef.push().key.toString()

            //database에 저장
            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title, content, uid, time))

            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()
            //이미지를 안올려도 검은화면이 나오는 것 방지 용
            if(isImageUpload == true){
                imageUpload(key)
            }
            finish()
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }
 //이미지 업로드 하는 부분
    private fun imageUpload(key : String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")
        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener {taskSnapshot ->

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }
    }
}
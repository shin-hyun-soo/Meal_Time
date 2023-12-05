package com.example.meal_time.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.meal_time.board.viewmodel.BoardViewModel
import com.example.meal_time.board.viewmodel.WriteState
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityBoardEditBinding

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key: String

    private lateinit var binding: ActivityBoardEditBinding
    private val viewModel by viewModels<BoardViewModel>()

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writerUid: String

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.writeState.collect { boardWriteState ->
                when (boardWriteState) {
                    WriteState.SUCCESS -> {
                        Toast.makeText(this@BoardEditActivity, "게시글 입력 완료", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }

                    WriteState.FAIL -> {
                        Toast.makeText(this@BoardEditActivity, "게시글 입력 실패", Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {}
                }
            }
        }

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            viewModel.editBoardData(
                key, BoardModel(
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    writerUid,

                    ),

                binding.imageArea
            )
        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }
    }

    private fun getImageData(key: String) {
        lifecycleScope.launch {
            val image = async { viewModel.getImageData(key) }.await() ?: return@launch
            Glide.with(this@BoardEditActivity)
                .load(image)
                .into(binding.imageArea)
        }
    }

    private fun getBoardData(key: String) {
        lifecycleScope.launch {
            val model = async { viewModel.getBoardData(key) }.await()
            binding.titleArea.setText(model?.title)
            binding.contentArea.setText(model?.content)
            writerUid = model?.uid ?: ""
        }
    }
}
package com.example.meal_time.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.meal_time.board.viewmodel.BoardViewModel
import com.example.meal_time.board.viewmodel.WriteState
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityBoardWriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private val viewModel by viewModels<BoardViewModel>()

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.writeState.collect { boardWriteState ->
                when (boardWriteState) {
                    WriteState.SUCCESS -> {
                        Toast.makeText(this@BoardWriteActivity, "게시글 입력 완료", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }

                    WriteState.FAIL -> {
                        Toast.makeText(this@BoardWriteActivity, "게시글 입력 실패", Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {}
                }
            }
        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

        binding.writeBtn.setOnClickListener {
            viewModel.write(
                binding.titleArea.text.toString(),
                binding.contentArea.text.toString(),

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
}
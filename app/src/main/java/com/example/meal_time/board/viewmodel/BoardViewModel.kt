package com.example.meal_time.board.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_time.board.BoardModel
import com.example.meal_time.board.repository.BoardRepository
import com.example.meal_time.comment.CommentModel
import com.example.meal_time.utils.FBAuth
import com.example.meal_time.utils.FBRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class WriteState {
    INIT, WRITE, FAIL, SUCCESS
}

class BoardViewModel : ViewModel() {

    private val repository = BoardRepository()

    private val _writeState = MutableSharedFlow<WriteState>()
    val writeState = _writeState.asSharedFlow()

    val _boardList = MutableSharedFlow<List<BoardModel>>()
    val boardList = _boardList.asSharedFlow()

    val _commentList = MutableSharedFlow<List<CommentModel>>()
    val commentList = _commentList.asSharedFlow()

    val keyList = MutableStateFlow<List<String>>(listOf())

    init {
        viewModelScope.launch {
            launch {
                repository.getBoardList().collect { list ->
                    _boardList.emit(list)
                }
            }
            launch {
                repository.getBoardKeyList().collect { list ->
                    keyList.emit(list)
                }
            }

        }
    }

    fun getComment(key: String) {
        viewModelScope.launch {
            repository.getCommentList(key).collect { list ->
                _commentList.emit(list)
            }
        }
    }

    fun write(title: String, content: String, imageView: ImageView?) {
        viewModelScope.launch {
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            val boardModel = BoardModel(title, content, uid, time)
            val key = FBRef.boardRef.push().key.toString()
            val result = async(Dispatchers.IO) {
                repository.writePost(key, boardModel, imageView)
            }.await()
            if (result) {
                _writeState.emit(WriteState.SUCCESS)
            } else {
                _writeState.emit(WriteState.FAIL)
            }
        }
    }

    fun editBoardData(key: String, data: BoardModel, imageView: ImageView?) {
        data.time = FBAuth.getTime()
        viewModelScope.launch {
            val result = async(Dispatchers.IO) {
                repository.writePost(key, data, imageView)
            }.await()
            if (result) {
                _writeState.emit(WriteState.SUCCESS)
            } else {
                _writeState.emit(WriteState.FAIL)
            }
        }
    }

    suspend fun getBoardData(key: String): BoardModel? {
        return repository.getBoardData(key)
    }

    suspend fun getImageData(key: String): Uri? {
        return repository.getImageData(key)
    }

    fun addComment(key: String, comment: CommentModel) {
        viewModelScope.launch {
            val result = repository.addComment(key, comment)
            if(result){
                Log.d("Board", "댓글 입력 성공")
            }
        }
    }

    fun deleteBoardData(key: String) {
        viewModelScope.launch {
            repository.removePost(key)
        }
    }
}

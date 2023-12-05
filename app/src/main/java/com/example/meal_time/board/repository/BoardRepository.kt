package com.example.meal_time.board.repository

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.example.meal_time.board.BoardModel
import com.example.meal_time.comment.CommentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.ByteArrayOutputStream
import java.util.concurrent.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class DataState<out R> {
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
}

class BoardRepository {
    private val storage = Firebase.storage
    private val database = Firebase.database.getReference("board")
    private val commentDatabase = Firebase.database.getReference("comment")

    fun getBoardList(): kotlinx.coroutines.flow.Flow<List<BoardModel>> {
        return callbackFlow<List<BoardModel>> {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val boards = mutableListOf<BoardModel>()
                    for (snap in snapshot.children) {
                        val item = snap.getValue<BoardModel>()
                        if (item != null) {
                            boards.add(item)
                        }
                    }
                    Log.d("BoardViewModel", boards.toString())
                    boards.reverse()
                    //데이터베이스에 넣어줌
                    trySend(boards)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            awaitClose()
        }
    }

    suspend fun getBoardKeyList(): kotlinx.coroutines.flow.Flow<List<String>> {
        return callbackFlow<List<String>> {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = mutableListOf<String>()
                    for (snap in snapshot.children) {
                        val item = snap.key
                        if (item != null) {
                            keys.add(item)
                        }
                    }
                    Log.d("BoardViewModel", keys.toString())
                    keys.reverse()
                    //데이터베이스에 넣어줌
                    trySend(keys)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            awaitClose()
        }
    }

    suspend fun getBoardData(key: String): BoardModel? {
        var boardModel: BoardModel? = null
        boardModel = suspendCoroutine {
            database.child(key).get().addOnSuccessListener { dataSnapshot ->
                boardModel = dataSnapshot.getValue<BoardModel>()
                if (boardModel != null) {
                    Log.d("BoardViewModel", boardModel.toString())
                    it.resume(boardModel)
                } else {
                    it.resume(null)
                }

            }
        }
        return boardModel
    }

    suspend fun writePost(key: String, boardModel: BoardModel, imageView: ImageView?): Boolean {
        val result = suspendCoroutine {
            database.child(key).setValue(boardModel).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    it.resume(true)
                    Log.d("Board", "EDIT SUCCESS")
                } else {
                    it.resume(false)
                    Log.d("Board", "EDIT FAIL")
                }
            }
            if (imageView != null) {
                uploadImage(key, imageView)
            }
        }
        return result
    }

    private fun uploadImage(key: String, imageView: ImageView) {
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    suspend fun getImageData(key: String): Uri? {
        val result = suspendCoroutine {
            try {
                storage.reference.child(key + ".png").downloadUrl.addOnSuccessListener { uri ->
                    it.resume(uri)
                }.addOnFailureListener { ex ->
                    it.resume(null)
                }
            } catch (e: StorageException) {
                it.resume(null)
            }
        }
        return result
    }

    suspend fun addComment(key: String, comment: CommentModel): Boolean {
        return suspendCoroutine { continuation ->
           commentDatabase.child(key).push().setValue(comment).addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }

    fun getCommentList(key: String): kotlinx.coroutines.flow.Flow<List<CommentModel>> {
        return callbackFlow<List<CommentModel>> {
            commentDatabase.child(key).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comments = mutableListOf<CommentModel>()
                    Log.d("Comment",snapshot.children.toString())
                    for (snap in snapshot.children) {
                        Log.d("Comment", snap.toString())
                        val item = snap.getValue<CommentModel>()
                        if (item != null) {
                            comments.add(item)
                        }
                    }
                    Log.d("BoardViewModel", comments.toString())
                    //데이터베이스에 넣어줌
                    trySend(comments)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            awaitClose()
        }
    }

    fun removePost(key: String) {
        database.child(key).removeValue()
    }
}
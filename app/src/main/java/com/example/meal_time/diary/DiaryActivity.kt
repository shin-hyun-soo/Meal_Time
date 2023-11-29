package com.example.meal_time.diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.example.meal_time.R

class DiaryActivity : AppCompatActivity() {
    // 메인 스레드에서 동작하는 핸들러를 생성합니다.
    private val handler = Handler(Looper.getMainLooper())
    // 지연 초기화를 사용하여 lazy하게 EditText를 초기화합니다.
    private val diaryEditText: EditText by lazy {
        findViewById<EditText>(R.id.diaryEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        // EditText 초기화 함수를 호출합니다.
        initDetailEditText()
    }
    // EditText를 초기화하고 저장된 텍스트를 불러옵니다.
    private fun initDetailEditText() {
        // "diary"라는 이름의 SharedPreferences에서 "detail" 키에 해당하는 값을 가져옵니다.
        val detail = getSharedPreferences("diary", Context.MODE_PRIVATE).getString("detail", "")
        // 가져온 값을 EditText에 설정합니다.
        diaryEditText.setText(detail)
        // "diary"라는 이름의 SharedPreferences에 "detail" 키에 현재 EditText의 텍스트를 저장합니다.
        val runnable = Runnable {
            // "diary"라는 이름의 SharedPreferences에 "detail" 키에 현재 EditText의 텍스트를 저장합니다.
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit(true) {
                putString("detail", diaryEditText.text.toString())
            }
        }

        diaryEditText.addTextChangedListener {
            // 텍스트가 변경될 때마다 로그를 출력합니다.
            Log.d("DiaryActivity", "text Changed :: $it")
            // 핸들러를 통해 일정 시간 동안 변경이 없을 때 저장 작업을 수행합니다.
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}
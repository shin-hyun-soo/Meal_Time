package com.example.meal_time.timer

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.meal_time.R
import com.example.meal_time.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    // 남은 분과 초를 나타내는 텍스트뷰들
    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }
    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }
    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }
    // 효과음을 재생하기 위한 SoundPool
    private val soundPool = SoundPool.Builder().build()
    // 현재 실행 중인 CountDownTimer, 효과음 ID 변수
    private var currentCountDownTimer: CountDownTimer? = null
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer)
        //setContentView(R.layout.activity_timer)
// 뷰들을 바인딩하고 효과음 초기화
        binding.btn3min.setOnClickListener {
            start3MinutesTimer()
        }
        binding.btn30min.setOnClickListener {
            start30MinutesTimer()
        }
        bindViews()
        initSounds()
    }
    private fun start3MinutesTimer(){
        stopCountDown()
        seekBar.progress = 3 // 3분으로 시크바 설정
        startCountDown()
    }
    private fun start30MinutesTimer(){
        stopCountDown()
        seekBar.progress = 30 // 30분으로 시크바 설정
        startCountDown()
    }
    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
    // 뷰들을 초기화하고 SeekBar의 리스너를 설정하는 함수
    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    if (seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }
        )
    }
    // 효과음을 초기화하는 함수
    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }
    // 주어진 시간을 갖는 CountDownTimer를 생성하는 함수
    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }
    // 타이머를 시작하는 함수
    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }
    // 타이머를 중지하는 함수
    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }
    // 타이머가 완료되었을 때 호출되는 함수
    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }
    // 주어진 시간을 갖는 남은 분과 초를 업데이트하는 함수
    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }
    // 주어진 시간을 갖는 SeekBar를 업데이트하는 함수
    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }
}

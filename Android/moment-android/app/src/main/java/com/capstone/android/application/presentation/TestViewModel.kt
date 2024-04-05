package com.capstone.android.application.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class TestViewModel {
}


class CountViewModel : ViewModel(){

    val time = 180*1000L
    private val _timeLeft = MutableStateFlow(time) // 3분을 초 단위로 초기화
    val timeLeft = _timeLeft.asStateFlow()

    private var countdownJob: Job? = null

    private var startTime = 0L // 카운트다운 시작 시간 (밀리초 단위)

    fun startCountdown() {
        if (countdownJob == null) {
            startTime = System.currentTimeMillis() // 현재 시간을 시작 시간으로 저장
            countdownJob = viewModelScope.launch {
                while (true) {
                    val elapsedTime =  System.currentTimeMillis() - startTime
                    val remainingTime = max(0, time - elapsedTime)
                    _timeLeft.value = remainingTime
                    Log.d("remainingTime", "startCountdown: $remainingTime")
                    delay(1000) // 1초마다 업데이트
                }
            }
        }
    }

    fun restartCountdown() {
        countdownJob?.cancel() // 기존의 카운트다운을 취소
        _timeLeft.value = time // 시간을 다시 초기값으로 설정
        updateCountdown() // 카운트다운 다시 시작
    }

    fun updateCountdown() {
        countdownJob?.cancel() // 이미 진행 중인 카운트다운을 취소
        startTime = System.currentTimeMillis() // 현재 시간을 시작 시간으로 저장
        countdownJob = viewModelScope.launch {
                while (true) {
                    val currentTime = System.currentTimeMillis()
                    val elapsedTime = currentTime - startTime
                    val remainingTime = max(0, time - elapsedTime)
                    _timeLeft.value = remainingTime
                    delay(1000) // 1초마다 업데이트

            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel() // ViewModel이 제거될 때 카운트다운을 중지
    }
}


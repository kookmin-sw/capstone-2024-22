package com.capstone.android.application.presentation


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.local.CustomNoTitleCheckDialogState
import com.capstone.android.application.data.local.CustomTitleCheckDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max


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


//제목없는 선택형 다이얼로그
@HiltViewModel
class CustomNoTitleCheckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle?,
) : ViewModel(){
    val CustomNoTitleCheckDialogState : MutableState<CustomNoTitleCheckDialogState> =
        mutableStateOf<CustomNoTitleCheckDialogState>(
            CustomNoTitleCheckDialogState()
        )
    fun showCustomNoTitleCheckDialog(){
        CustomNoTitleCheckDialogState.value = CustomNoTitleCheckDialogState(
            description = "앗 ! 지금 화면을 그냥 나가면 \n" +
                         "열심히 만든 영수증이 저장되지않아요",
            checkleft = "나갈게요",
            checkright = "들어갈게요",
            onClickCancel = { resetDialogState() },
            onClickleft = { },
            onClickright = { resetDialogState() },
        )
    }

    fun resetDialogState() {
        CustomNoTitleCheckDialogState.value = CustomNoTitleCheckDialogState()
    }
}

//제목 선택형 다이얼로그
@HiltViewModel
class CustomTitleCheckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle?,
) : ViewModel(){
    val CustomTitleCheckDialogState : MutableState<CustomTitleCheckDialogState> =
        mutableStateOf<CustomTitleCheckDialogState>(
            CustomTitleCheckDialogState()
        )
    fun showCustomTitleCheckDialog(){
        CustomTitleCheckDialogState.value = CustomTitleCheckDialogState(
            title = "2 개의 영수증을 정말 삭제 할까요?",
            description = "삭제된 영수증은 복구할 수 없어요",
            checkleft = "네",
            checkright = "아니요",
            onClickCancel = { resetDialogState() },
            onClickleft = { resetDialogState()  },
            onClickright = { resetDialogState() },
        )
    }

    fun resetDialogState() {
        CustomTitleCheckDialogState.value = CustomTitleCheckDialogState()
    }
}



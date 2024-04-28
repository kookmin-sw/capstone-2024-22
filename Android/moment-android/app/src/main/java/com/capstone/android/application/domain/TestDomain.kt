package com.capstone.android.application.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class TestDomain {
}

data class CustomNoTitleCheckDialogState(
    val description: String = "",
    val checkleft: String = "",
    val checkright: String = "",
    val onClickleft: () -> Unit = {},
    val onClickright: () -> Unit = {},
    val onClickCancel: () -> Unit = {},
)

data class CustomTitleCheckDialogState(
    val title: String = "",
    val description: String = "",
    val checkleft: String = "",
    val checkright: String = "",
    val onClickleft: () -> Unit = {},
    val onClickright: () -> Unit = {},
    val onClickCancel: () -> Unit = {},
)


//제목없는 선택형 다이얼로그
@HiltViewModel
class CustomNoTitleCheckViewModel @Inject constructor() : ViewModel(){
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
class CustomTitleCheckViewModel @Inject constructor() : ViewModel(){
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
package com.capstone.android.application.data.local

data class CustomTitleCheckDialogState(
    val title: String = "",
    val description: String = "",
    val checkleft: String = "",
    val checkright: String = "",
    val onClickleft: () -> Unit = {},
    val onClickright: () -> Unit = {},
    val onClickCancel: () -> Unit = {},
)

package com.capstone.android.application.domain.response

data class MomentErrorResponse(
    val errors: String,
    val reason: String,
    val resultMsg: String,
    val status: Int
)
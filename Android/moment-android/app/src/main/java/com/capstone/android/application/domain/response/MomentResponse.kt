package com.capstone.android.application.domain.response

data class MomentResponse(
    val code: String,
    val `data`: MomentData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
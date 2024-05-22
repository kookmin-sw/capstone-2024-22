package com.capstone.android.application.domain.response.auth

data class AuthResponse(
    val code: String,
    val `data`: AuthData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
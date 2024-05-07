package com.capstone.android.application.domain.response.auth

data class AuthData(
    val accessToken: String,
    val grantType: String,
    val refreshToken: String,
    val role: String,
    val userId : Int
)
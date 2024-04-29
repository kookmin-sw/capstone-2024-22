package com.capstone.android.application.data.remote.auth.login.response

import com.capstone.android.application.domain.response.AuthData

data class PostAuthLoginResponse(
    val code: String,
    val `data`: AuthData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
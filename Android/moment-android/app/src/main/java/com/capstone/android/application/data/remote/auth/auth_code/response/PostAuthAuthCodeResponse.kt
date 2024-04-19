package com.capstone.android.application.data.remote.auth.auth_code.response

import com.capstone.android.application.domain.response.AuthData

data class PostAuthAuthCodeResponse(
    val code: String,
    val `data`: AuthData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
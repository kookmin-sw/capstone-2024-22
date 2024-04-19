package com.capstone.android.application.data.remote.auth.auth_code_confirm.request

data class PatchAuthAuthCodeConfirmRequest(
    val userId : String,
    val code: String
)
package com.capstone.android.application.data.remote.auth.change_password.request

data class PatchAuthChangePasswordRequest(
    val code: String,
    val newPassword: String
)
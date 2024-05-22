package com.capstone.android.application.data.remote.setting.model

data class PatchFcmTokenRequest(
    val dataUsage: Boolean,
    val firebaseToken: String,
    val notification: Boolean
)
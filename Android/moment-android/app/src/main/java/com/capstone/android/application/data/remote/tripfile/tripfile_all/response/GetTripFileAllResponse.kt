package com.capstone.android.application.data.remote.tripfile.tripfile_all.response

data class GetTripFileAllResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
package com.capstone.android.application.domain.response.trip_file

data class TripFileResponse(
    val code: String,
    val `data`: TripFileData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
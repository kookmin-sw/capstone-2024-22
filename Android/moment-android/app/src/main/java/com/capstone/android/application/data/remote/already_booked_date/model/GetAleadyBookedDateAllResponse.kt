package com.capstone.android.application.data.remote.already_booked_date.model

data class GetAleadyBookedDateAllResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
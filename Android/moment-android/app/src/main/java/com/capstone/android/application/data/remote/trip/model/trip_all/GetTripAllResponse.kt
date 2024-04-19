package com.capstone.android.application.data.remote.trip.model.trip_all

data class GetTripAllResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
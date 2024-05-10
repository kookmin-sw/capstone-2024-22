package com.capstone.android.application.data.remote.trip.model.trip_detail

data class GetTripDetailResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
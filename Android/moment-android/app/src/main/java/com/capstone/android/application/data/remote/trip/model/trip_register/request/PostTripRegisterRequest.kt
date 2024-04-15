package com.capstone.android.application.data.remote.trip.model.trip_register.request

data class PostTripRegisterRequest(
    val endDate: String,
    val startDate: String,
    val tripName: String
)
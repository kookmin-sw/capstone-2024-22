package com.capstone.android.application.data.remote.trip.model.trip_put.request

data class PutTripRequest(
    val endDate: String,
    val startDate: String,
    val tripId: Int,
    val tripName: String
)
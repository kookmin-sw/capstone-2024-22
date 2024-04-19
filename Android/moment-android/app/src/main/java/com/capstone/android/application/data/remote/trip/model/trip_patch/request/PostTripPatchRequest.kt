package com.capstone.android.application.data.remote.trip.model.trip_patch.request

data class PostTripPatchRequest(
    val endDate: String,
    val startDate: String,
    val tripId: Int,
    val tripName: String
)
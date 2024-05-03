package com.capstone.android.application.domain.response.trip_file

data class TripFile(
    val totalCount: Int,
    val email: String,
    val id: Int,
    val tripId: Int,
    val yearDate: String
)
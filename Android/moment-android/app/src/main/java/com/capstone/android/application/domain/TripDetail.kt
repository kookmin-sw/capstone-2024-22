package com.capstone.android.application.domain

data class TripDetail(
    val analyzingCount: Int,
    val endDate: String,
    val id: Int,
    val numOfCard: Int,
    val sad: Double,
    val angry: Double,
    val happy: Double,
    val neutral: Double,
    val disgust: Double,
    val startDate: String,
    val tripName: String
)

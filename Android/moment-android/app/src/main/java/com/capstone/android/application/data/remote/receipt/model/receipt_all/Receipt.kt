package com.capstone.android.application.data.remote.receipt.model.receipt_all

data class Receipt(
    val id: Int,
    val tripId: Int,
    val tripName: String,
    val angry: Double,
    val disgust: Double,
    val happy: Double,
    val sad: Double,
    val neutral: Double,
    val stDate: String,
    val edDate: String,
    val numOfCard: Int,
    val mainDeparture: String,
    val subDeparture: String,
    val mainDestination: String,
    val subDestination: String,
    val oneLineMemo: String,
    val receiptThemeType: String,
    val createdAt : String

)
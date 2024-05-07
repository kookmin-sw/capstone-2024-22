package com.capstone.android.application.data.remote.receipt.model.receipt_post

data class PostReceiptCreateRequest(
    val mainDeparture: String,
    val mainDestination: String,
    val oneLineMemo: String,
    val receiptThemeType: String,
    val subDeparture: String,
    val subDestination: String,
    val tripId: Int
)
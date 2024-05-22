package com.capstone.android.application.data.remote.receipt.model.receipt_put

data class PutReceiptCreateRequest(
    val mainDeparture: String,
    val mainDestination: String,
    val oneLineMemo: String,
    val receiptThemeType: String,
    val subDeparture: String,
    val subDestination: String,
    val id: Int
)

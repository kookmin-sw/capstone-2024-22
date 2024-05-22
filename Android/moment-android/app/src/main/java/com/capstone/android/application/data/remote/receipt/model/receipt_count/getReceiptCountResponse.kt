package com.capstone.android.application.data.remote.receipt.model.receipt_count

data class getReceiptCountResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
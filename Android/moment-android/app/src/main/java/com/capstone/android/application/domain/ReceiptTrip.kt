package com.capstone.android.application.domain

data class ReceiptTrip(
    val id:Int,
    val tripName:String,
    val startDate:String,
    val endDate:String,
    val analyzingCount:Int,
    val numOfCard : Int
)

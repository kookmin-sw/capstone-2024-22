package com.capstone.android.application.domain.response.card

data class CardResponse(
    val code: String,
    val `data`: CardData,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
package com.capstone.android.application.data.remote.card.model.card_modify.request

data class PutCardModifyRequest(
    val location: String,
    val question: String,
    val stt: String,
    val temperature: String,
    val weather: String
)
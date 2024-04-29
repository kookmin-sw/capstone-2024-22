package com.capstone.android.application.domain.response.card

data class CardView(
    val angry: Double,
    val happy: Double,
    val id: Int,
    val location: String,
    val loved: Boolean,
    val neutral: Double,
    val question: String,
    val recordFileLength: Int,
    val recordFileName: String,
    val recordFileStatus: String,
    val recordFileUrl: String,
    val recordedAt: String,
    val sad: Double,
    val stt: String,
    val temperature: String,
    val tripFileId: Int,
    val weather: String
)
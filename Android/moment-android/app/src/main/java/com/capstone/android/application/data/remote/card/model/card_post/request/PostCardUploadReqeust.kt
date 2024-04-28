package com.capstone.android.application.data.remote.card.model.card_post.request

data class PostCardUploadReqeust(
    val location: String,
    val question: String,
    val recordedAt: String,
    val temperature: String,
    val weather: String
)
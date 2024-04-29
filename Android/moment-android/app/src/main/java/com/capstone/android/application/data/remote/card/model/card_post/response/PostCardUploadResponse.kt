package com.capstone.android.application.data.remote.card.model.card_post.response

data class PostCardUploadResponse(
    val code: String,
    val `data`: Data,
    val detailMsg: String,
    val msg: String,
    val status: Int
)
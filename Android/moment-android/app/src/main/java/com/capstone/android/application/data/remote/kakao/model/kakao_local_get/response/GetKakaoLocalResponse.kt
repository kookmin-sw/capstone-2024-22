package com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response

data class GetKakaoLocalResponse(
    val documents: List<Document>,
    val meta: Meta
)
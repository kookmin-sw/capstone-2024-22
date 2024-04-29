package com.capstone.android.application.data.remote.card

import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.data.remote.card.model.card_post.response.PostCardUploadResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.card.CardResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface CardRepositoryInterface {
    suspend fun postCardUpload(
        cardUploadMultipart: RequestBody,
        recordFile:MultipartBody.Part
    ):ApiResponse<PostCardUploadResponse>

    suspend fun deleteCard(
        cardViewId:Int
    ):Response<MomentResponse>

    suspend fun getCardAll(
        tripFileId:Int
    ):ApiResponse<CardResponse>

    suspend fun putCardModify(
        cardViewId:Int,
        body : PutCardModifyRequest
    ):Response<MomentResponse>

    suspend fun putCardLike(
        cardViewId:Int
    ):Response<MomentResponse>

    suspend fun getCardLiked():Response<CardResponse>
}
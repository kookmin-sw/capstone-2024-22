package com.capstone.android.application.data.remote.card

import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.data.remote.card.model.card_post.response.PostCardUploadResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.card.CardResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class CardRepository @Inject constructor(private val cardRetrofitInterface: CardRetrofitInterface):CardRepositoryInterface {
    override suspend fun postCardUpload(
        cardUploadMultipart: RequestBody,
        recordFile: MultipartBody.Part
    ): ApiResponse<PostCardUploadResponse> {
       return cardRetrofitInterface.postCardUpload(
            cardUploadMultipart = cardUploadMultipart,
            recordFile = recordFile
        )
    }

    override suspend fun deleteCard(cardViewId: Int): Response<MomentResponse> {
        return cardRetrofitInterface.deleteCard(
            cardViewId = cardViewId
        )
    }

    override suspend fun getCardAll(
        tripFileId:Int
    ): ApiResponse<CardResponse> {
        return cardRetrofitInterface.getCardAll(tripFileId = tripFileId)
    }

    override suspend fun putCardModify(
        cardViewId:Int,
        body : PutCardModifyRequest
    ): Response<MomentResponse> {
        return cardRetrofitInterface.putCardModify(
            cardViewId = cardViewId,
            body = body
        )
    }

    override suspend fun putCardLike(cardViewId: Int): Response<MomentResponse> {
        return cardRetrofitInterface.putCardLike(
            cardViewId = cardViewId
        )
    }

    override suspend fun getCardLiked(): Response<CardResponse> {
        return cardRetrofitInterface.getCardLiked()
    }
}
package com.capstone.android.application.data.remote.card

import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.domain.response.card.CardResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response

interface CardRepositoryInterface {
    suspend fun deleteCard(
        cardViewId:Int
    ):Response<MomentResponse>

    suspend fun getCardAll(
        tripFileId:Int
    ):Response<CardResponse>

    suspend fun putCardModify(
        cardViewId:Int,
        body : PutCardModifyRequest
    ):Response<MomentResponse>

    suspend fun putCardLike(
        cardViewId:Int
    ):Response<MomentResponse>

    suspend fun getCardLiked():Response<CardResponse>
}
package com.capstone.android.application.data.remote.card

import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.domain.response.card.CardResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CardRetrofitInterface {
    @DELETE("/core/cardView/{cardViewId}")
    suspend fun deleteCard(
        @Path(value = "cardViewId") cardViewId:Int
    ) : Response<MomentResponse>

    @GET("/core/cardView/all")
    suspend fun getCardAll():Response<CardResponse>

    @PUT("/core/cardView/{cardViewId}")
    suspend fun putCardModify(
        @Path(value="cardViewId") cardViewId:Int,
        @Body body : PutCardModifyRequest
    ):Response<MomentResponse>

    @PUT("/core/cardView/like/{cardViewId}")
    suspend fun putCardLike(
        @Path(value = "cardViewId") cardViewId:Int
    ):Response<MomentResponse>

    @GET("/core/cardView/like")
    suspend fun getCardLiked() : Response<CardResponse>

}
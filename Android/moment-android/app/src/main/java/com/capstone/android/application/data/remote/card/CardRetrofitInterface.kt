package com.capstone.android.application.data.remote.card

import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.data.remote.card.model.card_post.response.PostCardUploadResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.card.CardResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CardRetrofitInterface {

    @Multipart
    @POST("core/cardView/upload")
    suspend fun postCardUpload(
        @Part("uploadRecord") cardUploadMultipart:RequestBody,@Part recordFile : MultipartBody.Part
    ):ApiResponse<PostCardUploadResponse>


    @DELETE("/core/cardView/{cardViewId}")
    suspend fun deleteCard(
        @Path(value = "cardViewId") cardViewId:Int
    ) : Response<MomentResponse>

    @GET("/core/cardView/all/{tripFileId}")
    suspend fun getCardAll(
        @Path(value = "tripFileId") tripFileId:Int
    ):ApiResponse<CardResponse>

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
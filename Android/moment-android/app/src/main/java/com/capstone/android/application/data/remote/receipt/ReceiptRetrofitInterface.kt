package com.capstone.android.application.data.remote.receipt

import com.capstone.android.application.data.remote.receipt.model.receipt_all.getReceiptAllResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_count.getReceiptCountResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.deleteReceiptDeleteRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
interface ReceiptRetrofitInterface {

    @POST("/core/receipt")
    suspend fun postReceiptCreate(
        @Body body: PostReceiptCreateRequest
    ): ApiResponse<MomentResponse>

    @DELETE("/core/receipt/delete")
    suspend fun deleteReceiptDelete(
        @Body body: deleteReceiptDeleteRequest
    ) : ApiResponse<MomentResponse>

    @GET("/core/receipt/all")
    suspend fun getReceiptAll(
        @Query ("page") page : Int,
        @Query ("size") size : Int
    ) : ApiResponse<getReceiptAllResponse>

    @GET("/core/receipt/count")
    suspend fun getReceiptCount()
            : ApiResponse<getReceiptCountResponse>


}
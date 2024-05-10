package com.capstone.android.application.data.remote.receipt

import com.capstone.android.application.data.remote.receipt.model.receipt_all.getReceiptAllResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_count.getReceiptCountResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.deleteReceiptDeleteRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_put.PutReceiptCreateRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse

interface ReceiptRepositoryInterface {

    suspend fun postReceiptCreate(
        body : PostReceiptCreateRequest
    ): ApiResponse<MomentResponse>

    suspend fun deleteReceiptDelete(
        body: deleteReceiptDeleteRequest
    ): ApiResponse<MomentResponse>

    suspend fun getReceiptAll(
        page : Int,
        size : Int
    ): ApiResponse<getReceiptAllResponse>

    suspend fun getReceiptCount()
    : ApiResponse<getReceiptCountResponse>

    suspend fun putReceiptCreate(
        body : PutReceiptCreateRequest
    ): ApiResponse<MomentResponse>
}
package com.capstone.android.application.data.remote.receipt

import com.capstone.android.application.data.remote.card.CardRepositoryInterface
import com.capstone.android.application.data.remote.card.CardRetrofitInterface
import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.data.remote.card.model.card_post.response.PostCardUploadResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_all.getReceiptAllResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_count.getReceiptCountResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.deleteReceiptDeleteRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_put.PutReceiptCreateRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import javax.inject.Inject

class ReceiptRepository @Inject constructor(private val ReceiptRetrofitInterface: ReceiptRetrofitInterface):
    ReceiptRepositoryInterface {
    override suspend fun postReceiptCreate(
        body : PostReceiptCreateRequest
    ): ApiResponse<MomentResponse> {
        return ReceiptRetrofitInterface.postReceiptCreate(
            body = body
        )
    }

    override suspend fun deleteReceiptDelete(
        body: deleteReceiptDeleteRequest
    ): ApiResponse<MomentResponse> {
        return ReceiptRetrofitInterface.deleteReceiptDelete(
            body = body
        )
    }

    override suspend fun getReceiptAll(
        page : Int,
        size : Int
    ): ApiResponse<getReceiptAllResponse> {
        return ReceiptRetrofitInterface.getReceiptAll(
            page = page,
            size = size)
    }

    override suspend fun getReceiptCount(): ApiResponse<getReceiptCountResponse> {
        return ReceiptRetrofitInterface.getReceiptCount()
    }

    override suspend fun putReceiptCreate(
        body : PutReceiptCreateRequest
    ): ApiResponse<MomentResponse> {
        return ReceiptRetrofitInterface.putReceiptmodify(
            body = body
        )
    }
}
package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response
import javax.inject.Inject

class TripRepository @Inject constructor(private val tripRetrofitInterface:TripRetrofitInterface):TripRepositoryInterface {
    override suspend fun getTripAll(): ApiResponse<GetTripAllResponse> {
        return tripRetrofitInterface.getTripAll()
    }

    override suspend fun postTripRegister(
        body:PostTripRegisterRequest
    ): ApiResponse<MomentResponse> {
        return tripRetrofitInterface.postTripRegister(
                body = body
        )
    }

    override suspend fun deleteTrip(
        userId:Int
    ): ApiResponse<MomentResponse> {
        return tripRetrofitInterface.deleteTripDelete(userId = userId)
    }

    override suspend fun patchTrip(
        userId :Int,
        body: PostTripPatchRequest
    ): ApiResponse<MomentResponse> {
        return tripRetrofitInterface.patchTripPatch(userId = userId, body = body)
    }
}
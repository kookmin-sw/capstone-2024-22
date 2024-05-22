package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_detail.GetTripDetailResponse
import com.capstone.android.application.data.remote.trip.model.trip_put.request.PutTripRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse

interface TripRepositoryInterface {
    suspend fun getTripAll() : ApiResponse<GetTripAllResponse>
    suspend fun postTripRegister(
        body:PostTripRegisterRequest
    ) : ApiResponse<MomentResponse>

    suspend fun deleteTrip(
            userId:Int
    ) : ApiResponse<MomentResponse>

    suspend fun putTrip(
        body : PutTripRequest
    ) : ApiResponse<MomentResponse>

    suspend fun getTripDetail(
        tripId:Int
    ) : ApiResponse<GetTripDetailResponse>
}
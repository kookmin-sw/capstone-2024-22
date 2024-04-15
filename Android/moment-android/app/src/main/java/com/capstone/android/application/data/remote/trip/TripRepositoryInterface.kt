package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response

interface TripRepositoryInterface {
    suspend fun getTripAll() : Response<GetTripAllResponse>
    suspend fun postTripRegister(
        body:PostTripRegisterRequest
    ) : Response<MomentResponse>

    suspend fun deleteTrip(
            userId:Int
    ) : Response<MomentResponse>

    suspend fun patchTrip(
        userId : Int,
        body : PostTripPatchRequest
    ) : Response<MomentResponse>
}
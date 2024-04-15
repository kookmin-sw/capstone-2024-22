package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response
import javax.inject.Inject

class TripRepository @Inject constructor(private val tripRetrofitInterface:TripRetrofitInterface):TripRepositoryInterface {
    override suspend fun getTripAll(): Response<GetTripAllResponse> {
        return tripRetrofitInterface.getTripAll()
    }

    override suspend fun postTripRegister(
        body:PostTripRegisterRequest
    ): Response<MomentResponse> {
        return tripRetrofitInterface.postTripRegister(
                body = body
        )
    }

    override suspend fun deleteTrip(
        userId:Int
    ): Response<MomentResponse> {
        return tripRetrofitInterface.deleteTripDelete(userId = userId)
    }

    override suspend fun patchTrip(
        userId :Int,
        body: PostTripPatchRequest
    ): Response<MomentResponse> {
        return tripRetrofitInterface.patchTripPatch(userId = userId, body = body)
    }
}
package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface TripRetrofitInterface {
    @GET("core/trip/all")
    suspend fun getTripAll(): ApiResponse<GetTripAllResponse>

    @POST("core/trip/register")
    suspend fun postTripRegister(
        @Body body: PostTripRegisterRequest
    ): ApiResponse<MomentResponse>

    @DELETE("/core/trip")
    suspend fun deleteTripDelete(
        @Query("userId") userId:Int
    ) : ApiResponse<MomentResponse>

    @PATCH("/core/trip")
    suspend fun patchTripPatch(
        @Query("userId") userId: Int,
        @Body body : PostTripPatchRequest
    ) : ApiResponse<MomentResponse>
}
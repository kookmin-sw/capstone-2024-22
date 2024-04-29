package com.capstone.android.application.data.remote.trip

import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_put.request.PutTripRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TripRetrofitInterface {
    @GET("core/trip/all")
    suspend fun getTripAll(): ApiResponse<GetTripAllResponse>

    @POST("core/trip/register")
    suspend fun postTripRegister(
        @Body body: PostTripRegisterRequest
    ): ApiResponse<MomentResponse>

    @DELETE("/core/trip/{tripId}")
    suspend fun deleteTripDelete(
        @Path(value = "tripId") tripId : Int
    ) : ApiResponse<MomentResponse>

    @PUT("/core/trip")
    suspend fun putTripPatch(
        @Body body : PutTripRequest
    ) : ApiResponse<MomentResponse>
}
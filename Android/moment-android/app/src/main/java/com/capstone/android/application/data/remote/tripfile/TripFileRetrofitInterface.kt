package com.capstone.android.application.data.remote.tripfile

import com.capstone.android.application.data.remote.tripfile.tripfile_all.response.GetTripFileAllResponse
import com.capstone.android.application.domain.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TripFileRetrofitInterface {

    @GET("core/tripfile/{tripId}")
    suspend fun getTripFileAll(
        @Path(value = "tripId") tripId:Int
    ): ApiResponse<GetTripFileAllResponse>
}
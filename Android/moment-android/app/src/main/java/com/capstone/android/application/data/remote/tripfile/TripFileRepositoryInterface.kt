package com.capstone.android.application.data.remote.tripfile

import com.capstone.android.application.domain.response.trip_file.TripFileResponse
import com.capstone.android.application.domain.response.ApiResponse
import retrofit2.http.GET

interface TripFileRepositoryInterface {
    suspend fun getTripFileAll(
        tripId:Int
    ):ApiResponse<TripFileResponse>

    suspend fun getTripFileUntitled():ApiResponse<TripFileResponse>

}
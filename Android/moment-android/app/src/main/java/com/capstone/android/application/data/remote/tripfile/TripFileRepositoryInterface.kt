package com.capstone.android.application.data.remote.tripfile

import com.capstone.android.application.data.remote.tripfile.tripfile_all.response.GetTripFileAllResponse
import com.capstone.android.application.domain.response.ApiResponse

interface TripFileRepositoryInterface {
    suspend fun getTripFileAll(
        tripId:Int
    ):ApiResponse<GetTripFileAllResponse>

}
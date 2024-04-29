package com.capstone.android.application.data.remote.tripfile

import com.capstone.android.application.data.remote.tripfile.tripfile_all.response.GetTripFileAllResponse
import com.capstone.android.application.domain.response.ApiResponse
import javax.inject.Inject


class TripFileRepository @Inject constructor(private val tripFileRetrofitInterface: TripFileRetrofitInterface):TripFileRepositoryInterface {
    override suspend fun getTripFileAll(tripId: Int): ApiResponse<GetTripFileAllResponse> {
        return tripFileRetrofitInterface.getTripFileAll(
            tripId = tripId
        )
    }
}
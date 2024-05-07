package com.capstone.android.application.data.remote.tripfile

import com.capstone.android.application.domain.response.trip_file.TripFileResponse
import com.capstone.android.application.domain.response.ApiResponse
import javax.inject.Inject


class TripFileRepository @Inject constructor(private val tripFileRetrofitInterface: TripFileRetrofitInterface):TripFileRepositoryInterface {
    override suspend fun getTripFileAll(tripId: Int): ApiResponse<TripFileResponse> {
        return tripFileRetrofitInterface.getTripFileAll(
            tripId = tripId
        )
    }

    override suspend fun getTripFileUntitled(): ApiResponse<TripFileResponse> {
        return tripFileRetrofitInterface.getTripFileUntitled()
    }
}
package com.capstone.android.application.data.remote.already_booked_date

import com.capstone.android.application.data.remote.already_booked_date.model.GetAleadyBookedDateAllResponse
import com.capstone.android.application.domain.response.ApiResponse
import javax.inject.Inject

class AlreadyBookedDateRepository @Inject constructor(private val alreadyBookedDateRetrofitInterface: AlreadyBookedDateRetrofitInterface):AlreadyBookedDateRepositoryInterface {
    override suspend fun getAlreadyBookedDateAll(): ApiResponse<GetAleadyBookedDateAllResponse> {
        return alreadyBookedDateRetrofitInterface.getAlreadyBookedDateAll()
    }
}
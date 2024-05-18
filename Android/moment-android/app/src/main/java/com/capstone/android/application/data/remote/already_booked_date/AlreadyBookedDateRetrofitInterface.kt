package com.capstone.android.application.data.remote.already_booked_date

import com.capstone.android.application.data.remote.already_booked_date.model.GetAleadyBookedDateAllResponse
import com.capstone.android.application.domain.response.ApiResponse
import retrofit2.http.GET

interface AlreadyBookedDateRetrofitInterface {

    @GET("core/alreadyBookedDate/all")
    suspend fun getAlreadyBookedDateAll():ApiResponse<GetAleadyBookedDateAllResponse>
}
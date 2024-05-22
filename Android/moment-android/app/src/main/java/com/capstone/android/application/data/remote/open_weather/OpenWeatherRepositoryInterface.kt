package com.capstone.android.application.data.remote.open_weather

import com.capstone.android.application.data.remote.open_weather.model.response.GetWeatherResponse
import com.capstone.android.application.domain.response.ApiResponse

interface OpenWeatherRepositoryInterface {
    suspend fun getWeatherInCurrentLocation(
        lat:String,
        lon:String,
        appid:String
    ): ApiResponse<GetWeatherResponse>
}
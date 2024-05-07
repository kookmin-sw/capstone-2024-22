package com.capstone.android.application.data.remote.open_weather

import com.capstone.android.application.data.remote.open_weather.model.response.GetWeatherResponse
import com.capstone.android.application.domain.response.ApiResponse
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(private val openWeatherRetrofitInterface: OpenWeatherRetrofitInterface):OpenWeatherRepositoryInterface {
    override suspend fun getWeatherInCurrentLocation(
        lat: String,
        lon: String,
        appid: String
    ): ApiResponse<GetWeatherResponse> {
        return openWeatherRetrofitInterface.getWeatherInCurrentLocation(
            lat = lat,
            lon = lon,
            appid = appid
        )
    }
}
package com.capstone.android.application.data.remote.open_weather

import com.capstone.android.application.data.remote.open_weather.model.response.GetWeatherResponse
import com.capstone.android.application.domain.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherRetrofitInterface {
    @GET("data/2.5/weather")
    suspend fun getWeatherInCurrentLocation(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") appid:String
    ):ApiResponse<GetWeatherResponse>


}
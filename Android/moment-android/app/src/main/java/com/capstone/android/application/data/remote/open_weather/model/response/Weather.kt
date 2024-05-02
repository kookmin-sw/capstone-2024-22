package com.capstone.android.application.data.remote.open_weather.model.response

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
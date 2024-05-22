package com.capstone.android.application.app.utile.common

import com.capstone.android.application.R

fun GetWeatherType(weather:String):String{
    when(weather){
        "Thunderstorm" -> { return "번개" }
        "Drizzle" -> { return "이슬비" }
        "Rain" -> { return "비" }
        "Snow" -> { return "눈" }
        "Atmosphere" -> { return "안개낀" }
        "Clear" -> { return "맑음" }
        "Clouds" -> { return "흐림" }
        else -> { return "null" }
    }

}

fun GetWeatherIconDrawableCode(weather:String):Int{
    when(weather){
        "번개" -> { return R.drawable.ic_weather_thunderstorm }
        "이슬비" -> { return R.drawable.ic_weather_shower_rain }
        "비" -> { return R.drawable.ic_weather_rain }
        "눈" -> { return R.drawable.ic_weather_snow }
        "안개낀" -> { return R.drawable.ic_weather_mist }
        "맑음" -> { return R.drawable.ic_weather_clear }
        "흐림" -> { return R.drawable.ic_weather_cloud }
        else -> { return R.drawable.ic_weather_clear }
    }
}
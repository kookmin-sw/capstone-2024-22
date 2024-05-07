package com.capstone.android.application.app.utile.common

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
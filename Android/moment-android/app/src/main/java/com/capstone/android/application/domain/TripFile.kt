package com.capstone.android.application.domain

import androidx.compose.runtime.MutableState

data class TripFile(

    val id:Int,
    val tripId:Int,
    val yearDate:String,
    var analyzingCount:MutableState<Int>
)
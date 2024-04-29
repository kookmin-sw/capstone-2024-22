package com.capstone.android.application.app.screen

sealed class MainScreen(
    val title: Int,val screenRoute: String,val rootRoute:String , val label:String
){

    object ReceiptPost : MainScreen(title = 5, screenRoute = "ReceiptPost", rootRoute = "Receipt", label = "영수증 보기")
    object HomeTrip : MainScreen(title = 7, screenRoute = "HomeTrip", rootRoute = "Home" ,label = "홈 여행")
    object RecordDaily : MainScreen(title = 8, screenRoute = "RecordDaily", rootRoute = "Home", label = "일상기록")
}
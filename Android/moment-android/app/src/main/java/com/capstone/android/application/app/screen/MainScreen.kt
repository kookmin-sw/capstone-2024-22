package com.capstone.android.application.app.screen

sealed class MainScreen(
    val title: Int,val screenRoute: String,val rootRoute:String , val label:String
){

    object ReceiptPost : MainScreen(title = 5, screenRoute = "ReceiptPost", rootRoute = "Receipt", label = "영수증 만들기")
    object ReceiptCardChoice : MainScreen(title = 6, screenRoute = "ReceiptCardChoice", rootRoute = "Receipt", label = "카드 선택")
    object HomeTrip : MainScreen(title = 7, screenRoute = "HomeTrip", rootRoute = "Home" ,label = "홈 여행")
    object RecordDaily : MainScreen(title = 8, screenRoute = "RecordDaily", rootRoute = "Home", label = "일상기록")
}
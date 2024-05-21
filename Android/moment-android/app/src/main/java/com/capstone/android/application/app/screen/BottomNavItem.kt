package com.capstone.android.application.app.screen

import com.capstone.android.application.R

sealed class BottomNavItem(
    val title: Int,val screenRoute: String, val label:String,val selectedDrawableId:Int , val unselectedDrawableId:Int
) {
    object Home : BottomNavItem(title = 0 , screenRoute = "Home",label = "홈", selectedDrawableId = R.drawable.ic_nav_home_black, unselectedDrawableId = R.drawable.ic_nav_home_grey)
    object Receipt : BottomNavItem(title = 1 , screenRoute = "Receipt",label = "여행티켓",selectedDrawableId = R.drawable.ic_nav_receipt_black, unselectedDrawableId = R.drawable.ic_nav_receipt_grey)
    object Record : BottomNavItem(title = 2 , screenRoute = "Record",label = "녹음",selectedDrawableId = 0, unselectedDrawableId = 0)
    object Favorite : BottomNavItem(title = 3 , screenRoute = "Favorite",label = "즐겨찾기",selectedDrawableId = R.drawable.ic_nav_heart_black, unselectedDrawableId = R.drawable.ic_nav_heart_grey)
    object Setting : BottomNavItem(title = 4 , screenRoute = "Setting",label = "세팅",selectedDrawableId = R.drawable.ic_nav_setting_black, unselectedDrawableId = R.drawable.ic_nav_setting_grey)
}
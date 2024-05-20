package com.capstone.android.application.ui

import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.app.ApplicationClass.Companion.tokenSharedPreferences
import com.capstone.android.application.ui.theme.BigButton
import com.capstone.android.application.ui.theme.P_Bold
import com.capstone.android.application.ui.theme.PretendardFamily
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_100
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.tertiary_500
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint


enum class SplashScreen(){
    Splash,
    Intro
}

@AndroidEntryPoint
class SplashActivity:ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!tokenSharedPreferences.getString("accessToken","").isNullOrEmpty()){
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }

        setContent{
            navController = rememberNavController()
            val movenav = try {
                intent.getStringExtra("MoveScreen")
            }catch (e : Exception){
                "Basic"
            }


            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(navController = navController,
                    startDestination = when(movenav){
                        "intro" -> SplashScreen.Intro.name
                        else -> SplashScreen.Splash.name} ){
                    composable(route=SplashScreen.Splash.name){ Splash() }
                    composable(route=SplashScreen.Intro.name){ Intro() }
                }
            }
        }
    }
    @Composable
    fun Splash() {
        LogoScreen()
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(SplashScreen.Intro.name)
        }, 1000)
    }

    @Composable
    fun LogoScreen(){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(tertiary_500),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id =R.drawable.img_logo),
                contentDescription = ""
            )
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Intro() {

        val state = rememberPagerState()
        val image1 = painterResource(R.drawable.img_splash_1)
        val image2 = painterResource(R.drawable.img_splash_2)
        val image3 = painterResource(R.drawable.img_splash_3)
        val image4 = painterResource(R.drawable.img_splash_4)

        val ment1 = "언제 어디서든\n" +
                "빠르고 간편하게 연동!"
        val ment2 = "혼자하는 여행도\n" +
                "적적하지 않아"
        val ment3 = "가득한 여행 기록도\n" +
                "깔끔하게 정리"
        val ment4 = "소중한 추억들\n" +
                "예쁘게 간직하기"

        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(tertiary_500),
                verticalArrangement = Arrangement.Center) {
                HorizontalPager(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .height(550.dp)
                        .fillMaxWidth(),
                    count = 4,
                    state = state
                ) { page ->
                    if (page == 0) {
                        Column{
                            Intro_Screen(state,image1,"right",ment1)
                        }
                    }
                    if (page == 1) {
                        Column() {
                            Intro_Screen(state, image2,"left",ment2)
                        }
                    }
                    if (page == 2) {
                        Column() {
                            Intro_Screen(state, image3,"right",ment3)
                        }
                    }
                    if (page == 3) {
                        Column() {
                            Intro_Screen(state, image4,"left",ment4)
                        }
                    }
                }
                if(state.currentPage == 3){
                    DotsIndicator(totalDots = 4, selectedIndex = state.currentPage)
                    Column(Modifier.padding(horizontal = 20.dp)){
                        Spacer(modifier = Modifier.height(5.dp))
                        BigButton("시작하기", true,
                            onClick = {startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))}
                        )
                    }
                }else{
                    DotsIndicator(totalDots = 4, selectedIndex = state.currentPage)
                    Column(Modifier
                            .padding(horizontal = 20.dp)
                            .align(Alignment.End)) {
                        Spacer(modifier = Modifier.height(5.dp))
                        SmallStartBtn()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Intro_Screen(state: PagerState, image: Painter, location: String, ment :String){
        Box( ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = tertiary_500)) {
                Image(painter = image,
                    contentDescription = "둘러보기 자료",
                    modifier = Modifier
                        .fillMaxWidth())
            }
            if(location == "left"){
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 60.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start) {
                P_Bold(ment, black, 22.sp, TextAlign.Start)
            }}
            else{
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(end = 60.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End) {
                    P_Bold(ment, black, 22.sp, TextAlign.End)
                }
            }
        }

    }

    @Composable
    fun DotsIndicator(totalDots: Int, selectedIndex: Int){
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {

            items(totalDots) { index ->
                if (index == selectedIndex) {
                    Box(
                        modifier = Modifier
                            .size(width = 20.dp, height = 3.dp)
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(color = primary_500)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(width = 20.dp, height = 3.dp)
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(color = neutral_100)
                    )
                }

                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                }
            }
        }
    }

    @Composable
    fun SmallStartBtn(){
        Row (modifier = Modifier
            .wrapContentSize()
            .clickable {
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                finish()
            },
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .padding(vertical = 4.dp),
                text = "바로 시작하기",
                style = TextStyle(
                    color = black,
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp)
            )
            Image(painter = painterResource(R.drawable.ic_btn_start),
                contentDescription = "바로 시작 버튼",
                modifier = Modifier
                    .width(15.dp)
                    .height(15.dp)
                    .padding(end = 4.dp))
        }
    }


    @Preview
    @Composable
    fun SplashPreview(){
        Intro()
    }
}

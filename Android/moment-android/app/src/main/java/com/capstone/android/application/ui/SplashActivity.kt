package com.capstone.android.application.ui

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.android.application.R
import com.capstone.android.application.ui.theme.BigButton
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

        setContent{
            navController = rememberNavController()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(navController = navController, startDestination = SplashScreen.Splash.name ){
                    composable(route=SplashScreen.Splash.name){ Splash() }
                    composable(route=SplashScreen.Intro.name){ Intro() }
                }
            }
        }
    }

    @Composable
    fun Splash(){
        Surface {
            Text(
                modifier = Modifier.clickable { navController.navigate(SplashScreen.Intro.name) },
                text = "로고"
            )
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Intro() {

        val state = rememberPagerState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(tertiary_500)) {
            HorizontalPager(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(top = 87.dp)
                    .height(600.dp)
                    .fillMaxWidth(),
                count = 4,
                state = state
            ) { page ->
                if (page == 0) {
                    Column(
                        Modifier.padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Intro_Screen(state)

                    }
                }
                if (page == 1) {
                    Column(
                        Modifier.padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Intro_Screen(state)
                    }
                }
                if (page == 2) {
                    Column(
                        Modifier.padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Intro_Screen(state)

                    }
                }
                if (page == 3) {
                    Column(
                        Modifier.padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Intro_Screen(state)

                    }
                }
            }
            if(state.currentPage == 3){
                Column(Modifier.padding(horizontal = 20.dp)){
                    Spacer(modifier = Modifier.height(8.dp))
                    BigButton("시작하기", true,
                        onClick = {startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))}
                    )
                }
            }else{
                Column(Modifier.padding(horizontal = 20.dp)
                    .align(Alignment.End)) {
                    Spacer(modifier = Modifier.height(12.dp))
                    SmallStartBtn()
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Intro_Screen(state: PagerState){
        Column(){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Gray)
            ) {
                Image(painter = painterResource(R.drawable.img_intro_picture),
                    contentDescription = "둘러보기 자료",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(592.dp))

                Column {
                    Spacer(modifier = Modifier.height(569.dp))
                    DotsIndicator(totalDots = 4, selectedIndex = state.currentPage)
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

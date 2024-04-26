package com.capstone.android.application.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.ui.theme.ApplicationTheme
import com.capstone.android.application.ui.theme.ImgBackButton
import com.capstone.android.application.ui.theme.P_Black45
import com.capstone.android.application.ui.theme.P_Black50
import com.capstone.android.application.ui.theme.P_ExtraBold14
import com.capstone.android.application.ui.theme.P_Medium11
import com.capstone.android.application.ui.theme.P_Medium14
import com.capstone.android.application.ui.theme.P_Medium8
import com.capstone.android.application.ui.theme.ReciptTextField
import com.capstone.android.application.ui.theme.YJ_Bold15
import com.capstone.android.application.ui.theme.YJ_Bold20
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_100
import com.capstone.android.application.ui.theme.neutral_300
import com.capstone.android.application.ui.theme.neutral_500
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.primary_200
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.tertiary_500
import com.capstone.android.application.ui.theme.white
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


enum class ReciptScreen(){
    TripChoice,
    Horizontal_Theme,
    SaveRecipt
    ReceiptPost_Big,
    EditReceipt,
    SaveEditReceipt
}

class ReciptActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            val movenav = intent.getStringExtra("MoveScreen")

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {

                }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController, startDestination = ReciptScreen.TripChoice.name
                ) {
                    composable(route = ReciptScreen.TripChoice.name) { TripChoice() }
                    composable(route = ReciptScreen.Horizontal_Theme.name) { Horizontal_Theme() }
                    composable(route = ReciptScreen.SaveRecipt.name) { SaveRecipt("theme1") }
                    composable(route = ReciptScreen.ReceiptPost_Big.name) { ReceiptPost_Big() }
                    composable(route = ReciptScreen.EditReceipt.name) { EditReceipt () }
                    composable(route = ReciptScreen.SaveEditReceipt.name) { SaveEditReceipt ("theme1") }
                }
            }
        }
    }

    data class Emotion(
        val icon: Int,
        val text: String,
        val persent: String
    )

    @Composable
    fun TripChoice(){
        Log.d("where", "TripChoice: ")
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color("#C3C1C1".toColorInt()))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(734.dp)
                    .padding(start = 18.dp)
                    .align(Alignment.Center)
                    .background(color = tertiary_500)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 23.dp, start = 8.dp)
                        .wrapContentSize()
                ) {
                    ImgBackButton(onClick = {startActivity(Intent(this@ReciptActivity, MainActivity::class.java))}, "여행 선택하기")
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 11.dp)
                        .wrapContentSize()
                        .verticalScroll(scrollState)
                ) {
                    for (i in 0..10){
                        ItemTrip()
                        Column(Modifier.padding(start = 16.dp, end = 9.dp)) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider(color = neutral_300)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemTrip(){
        Column(
            modifier = Modifier
                .background(color = tertiary_500)
                .clickable { navController.navigate(ReciptScreen.Horizontal_Theme.name) },
        ) {
            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp)
                        .background(color = tertiary_500),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column {
                        P_Medium11("2024.03.05", black)
                        Spacer(modifier = Modifier.height(6.dp))
                        P_Medium11("2024.03.05", black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = primary_500,
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    P_ExtraBold14("전라도의 선유도", black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = primary_500,
                        thickness = 2.dp
                    )
                }
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Horizontal_Theme(){
        val page = 2
        val state = rememberPagerState()

        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }

        val emotionList = mutableStateListOf<Emotion>()

        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = "60%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = "20%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = "15%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요 ",
                persent = "5%"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { navController.navigate(ReciptScreen.TripChoice.name) }) {
                    YJ_Bold15("뒤로", black)
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { navController.navigate(ReciptScreen.SaveRecipt.name) }) {
                    YJ_Bold15("완료", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalPager(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .height(651.dp)
                    .fillMaxWidth(),
                count = page,
                state = state
            ) { page ->
                if (page == 0) {
                    Column(Modifier.padding(horizontal = 20.dp) ){
                        EditTripTheme1(intro, depart_small, depart, arrive_small, arrive, emotionList)
                    }
                }
                if (page == 1) {
                    Column(Modifier.padding(horizontal = 20.dp)){
                        EditTripTheme2(intro, depart_small, depart, arrive_small, arrive, emotionList)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DotsIndicator(totalDots = page, selectedIndex = state.currentPage)
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun EditTripTheme1(
        intro: MutableState<String>,
        depart_small: MutableState<String>,
        depart: MutableState<String>,
        arrive_small: MutableState<String>,
        arrive: MutableState<String>,
        emotionList: SnapshotStateList<Emotion>
    ) {

        Box(
            modifier = Modifier
                .height(651.dp)
                .fillMaxWidth()
                .background(
                    color = white,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    BorderStroke((0.5).dp, black),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                        )
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    P_Medium14(content = "암스테르담 성당 여행", color = white)
                    Image(
                        painter = painterResource(R.drawable.img_logo_white),
                        contentDescription = "로고 화이트"
                    )
                }

                Column(
                    modifier = Modifier.height(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(1.dp))
                    P_Medium8(
                        " 티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. ", primary_200
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReciptTextField(
                        hint = "여행의 기록을 한 줄로 기록하세요:)",
                        onValueChanged = { intro.value = it },
                        text = intro,
                        keyboardType = KeyboardType.Text,
                        textcolor = neutral_500,
                        fontweight = FontWeight.Medium,
                        fontsize = 14.sp,
                        type = "intro"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 136.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (depart_small.value.isNotEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                ReciptTextField(
                    hint = "북촌 한옥마을",
                    onValueChanged = {depart_small.value = it },
                    text = depart_small,
                    keyboardType = KeyboardType.Text,
                    textcolor = primary_500,
                    fontweight = FontWeight.Medium,
                    fontsize = 14.sp,
                    type = "small"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 156.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReciptTextField(
                    hint = "서울",
                    onValueChanged = {depart.value = it },
                    text = depart,
                    keyboardType = KeyboardType.Text,
                    textcolor = primary_500,
                    fontweight = FontWeight.Black,
                    fontsize = 50.sp,
                    type = "big"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 246.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_airplain_red),
                    contentDescription = "장소"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 312.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (arrive_small.value.isNotEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                ReciptTextField(
                    hint = "암스테르담 공항",
                    onValueChanged = { arrive_small.value = it },
                    text = arrive_small,
                    keyboardType = KeyboardType.Text,
                    textcolor = primary_500,
                    fontweight = FontWeight.Medium,
                    fontsize = 14.sp,
                    type = "small"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 333.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReciptTextField(
                    hint = "암스테르담",
                    onValueChanged = {arrive.value = it },
                    text = arrive,
                    keyboardType = KeyboardType.Text,
                    textcolor = primary_500,
                    fontweight = FontWeight.Black,
                    fontsize = 50.sp,
                    type = "big"
                )
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp, top = 450.dp)) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_red),
                    contentDescription = "가위",
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 41.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    Modifier
                        .height(150.dp)
                        .padding(end = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium11(content = "여행 카드", color = neutral_500)
                    Spacer(modifier = Modifier.height(10.dp))
                    P_ExtraBold14(content = "27", color = primary_500)
                    Spacer(modifier = Modifier.height(30.dp))
                    P_Medium11(content = "여행 날짜", color = neutral_500)
                    Spacer(modifier = Modifier.height(6.dp))
                    P_Medium11(content = "2024.02.15", color = primary_500)
                    Spacer(modifier = Modifier.height(5.dp))
                    P_Medium11(content = "2024.02.15", color = primary_500)
                }
                Spacer(modifier = Modifier.width(50.dp))
                Column() {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            emotionList.forEach { item ->
                                Row(
                                    modifier = Modifier.height(25.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.width(109.dp)
                                    )  {
                                        LinearProgressIndicator(
                                            progress = { 0.4f },
                                            modifier = Modifier.height(8.dp),
                                            color = primary_500,
                                            strokeCap = StrokeCap.Round
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        modifier = Modifier.size(12.dp),
                                        painter = painterResource(id = item.icon),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    P_Medium11(content = "60%", color = primary_500)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EditTripTheme2(
        intro: MutableState<String>,
        depart_small: MutableState<String>,
        depart: MutableState<String>,
        arrive_small: MutableState<String>,
        arrive: MutableState<String>,
        emotionList: SnapshotStateList<Emotion>
    ) {

        Box(
            modifier = Modifier
                .height(577.dp)
                .fillMaxWidth()
                .background(
                    color = white,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    BorderStroke((0.5).dp, neutral_500),
                    shape = RoundedCornerShape(5.dp)
                )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp, start = 36.dp)
            ) {
                ReciptTextField(
                    hint = "여행의 기록을 한 줄로 기록하세요:)",
                    onValueChanged = { intro.value = it },
                    text = intro,
                    keyboardType = KeyboardType.Text,
                    textcolor = neutral_500,
                    fontweight = FontWeight.Medium,
                    fontsize = 14.sp,
                    type = "intro"
                )
            }

            Column(Modifier.padding(top = 51.dp)) {
                Divider(color = primary_500, thickness = 2.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 54.dp, start = 30.dp)

            ) {
                P_Medium8(
                    " 티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. ", primary_200
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 430.dp, start = 28.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Medium14(content = "전라도의 선유도", color = black)
            }

            Column(
                modifier = Modifier
                    .width(10.dp)
                    .padding(top = 52.dp)
                    .fillMaxHeight()
                    .background(
                        color = primary_500,
                        shape = RoundedCornerShape(bottomEnd = 5.dp)
                    )
                    .align(Alignment.BottomEnd)
            ) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (depart_small.value.isNotEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_location_grey),
                        contentDescription = "장소",
                        Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                ReciptTextField(
                    hint = "북촌 한옥마을",
                    onValueChanged = { depart_small.value = it },
                    text = depart_small,
                    keyboardType = KeyboardType.Text,
                    textcolor = neutral_600,
                    fontweight = FontWeight.Medium,
                    fontsize = 11.sp,
                    type = "small1"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 115.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReciptTextField(
                    hint = "서울",
                    onValueChanged = {depart.value = it },
                    text = depart,
                    keyboardType = KeyboardType.Text,
                    textcolor = black,
                    fontweight = FontWeight.Black,
                    fontsize = 45.sp,
                    type = "big"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 179.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_train_grey),
                    contentDescription = "장소"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 281.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (depart_small.value.isNotEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_location_grey),
                        contentDescription = "장소",
                        Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                ReciptTextField(
                    hint = "암스테르담 공항",
                    onValueChanged = {arrive_small.value = it },
                    text = arrive_small,
                    keyboardType = KeyboardType.Text,
                    textcolor = neutral_600,
                    fontweight = FontWeight.Medium,
                    fontsize = 11.sp,
                    type = "small1"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 297.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReciptTextField(
                    hint = "군산",
                    onValueChanged = {arrive.value = it },
                    text = arrive,
                    keyboardType = KeyboardType.Text,
                    textcolor = black,
                    fontweight = FontWeight.Black,
                    fontsize = 45.sp,
                    type = "big"
                )
            }

            Row(
                Modifier
                    .padding(bottom = 41.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Column(
                    Modifier.width(107.dp)
                ) {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                }
                Column(
                    Modifier.padding(start = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium11(content = "카드 갯수", color = neutral_500)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        Modifier
                            .size(60.dp)
                            .background(
                                color = white,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = black,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        YJ_Bold20(content = "27", color = primary_500)
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = 1.dp, end = 18.dp)
                    .align(Alignment.BottomEnd)
            ) {
                P_Medium11(content = "2024 . 02 . 15", color = neutral_500)
                P_Medium11(content = " / ", color = neutral_500)
                P_Medium11(content = "2024 . 02 . 15", color = neutral_500)
            }


            Row() {
                Column(
                    modifier = Modifier
                        .width(28.dp)
                        .fillMaxHeight()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_logo_vertical),
                        contentDescription = "로고",
                        Modifier
                            .width(22.dp)
                            .fillMaxHeight()
                    )
                }
            }

            Column(Modifier.padding(top = 400.dp)) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_circle),
                    contentDescription = "로고",
                    Modifier
                        .height(25.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun DotsIndicator(totalDots: Int, selectedIndex: Int) {
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
                            .background(color = neutral_500)
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
    fun SaveRecipt(chosenTheme: String){

        val chosenTheme  = "theme1"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = neutral_300)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { }) {
                    YJ_Bold15("내보내기", black)
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable {
                            startActivity(
                                Intent(
                                    this@ReciptActivity,
                                    MainActivity::class.java
                                )
                            )
                        }) {
                    YJ_Bold15("완료", primary_500)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 앞에서 불러온 화면 데이터 받아와서 수정안되는 버전으로 넣기
            if (chosenTheme == "theme1") {
                SaveTheme1()
            }
            if(chosenTheme == "theme2"){
                SaveTheme2()

            }
        }
    }

    @Composable
    fun SaveTheme1(){
        Box(
            modifier = Modifier
                .height(651.dp)
                .fillMaxWidth()
                .background(
                    color = white,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    BorderStroke((0.5).dp, black),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                        )
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    P_Medium14(content = "암스테르담 성당 여행", color = white)
                    Image(
                        painter = painterResource(R.drawable.img_logo_white),
                        contentDescription = "로고 화이트"
                    )
                }

                Column(
                    modifier = Modifier.height(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(1.dp))
                    P_Medium8(
                        " 티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                                "티켓이 발행된 날짜는 2024 02 15 입니다. ", primary_200
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium14(content = "여행의 기록을 한 줄로 기록하세요:)", color = neutral_500)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 136.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium14(content = "북촌한옥마을", color = primary_500)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 156.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_Black50("서울", primary_500)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 246.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_airplain_red),
                    contentDescription = "장소"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 312.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium14(content = "암스테르담 공항", color = primary_500)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 333.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_Black50(content = "암스테르담", color = primary_500)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp, top = 450.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_red),
                    contentDescription = "가위",
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 41.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    Modifier
                        .height(150.dp)
                        .padding(end = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium11(content = "여행 카드", color = neutral_500)
                    Spacer(modifier = Modifier.height(10.dp))
                    P_ExtraBold14(content = "27", color = primary_500)
                    Spacer(modifier = Modifier.height(30.dp))
                    P_Medium11(content = "여행 날짜", color = neutral_500)
                    Spacer(modifier = Modifier.height(6.dp))
                    P_Medium11(content = "2024.02.15", color = primary_500)
                    Spacer(modifier = Modifier.height(5.dp))
                    P_Medium11(content = "2024.02.15", color = primary_500)
                }
                Spacer(modifier = Modifier.width(50.dp))
                Column() {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        /*Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            emotionList.forEach { item ->
                                Row(
                                    modifier = Modifier.height(25.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.width(109.dp)
                                    )  {
                                        LinearProgressIndicator(
                                            progress = { 0.4f },
                                            modifier = Modifier.height(8.dp),
                                            color = primary_500,
                                            strokeCap = StrokeCap.Round
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        modifier = Modifier.size(12.dp),
                                        painter = painterResource(id = item.icon),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    P_Medium11(content = "60%", color = primary_500)
                                }
                            }
                        }*/
                    }
                }
            }
        }
    }

    @Composable
    fun SaveTheme2(){
        Box(
            modifier = Modifier
                .height(577.dp)
                .fillMaxWidth()
                .background(
                    color = white,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    BorderStroke((0.5).dp, neutral_500),
                    shape = RoundedCornerShape(5.dp)
                )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp, start = 36.dp)
            ) {
                P_Medium14(content = "여행의 기록을 한 줄로 기록하세요:)", color = neutral_500)
            }

            Column(Modifier.padding(top = 51.dp)) {
                Divider(color = primary_500, thickness = 2.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 54.dp, start = 30.dp)

            ) {
                P_Medium8(
                    " 티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. " +
                            "티켓이 발행된 날짜는 2024 02 15 입니다. ", primary_200
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 430.dp, start = 28.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Medium14(content = "전라도의 선유도", color = black)
            }

            Column(
                modifier = Modifier
                    .width(10.dp)
                    .padding(top = 52.dp)
                    .fillMaxHeight()
                    .background(
                        color = primary_500,
                        shape = RoundedCornerShape(bottomEnd = 5.dp)
                    )
                    .align(Alignment.BottomEnd)
            ) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium11(content = "북촌 한옥마을", color =neutral_600)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 115.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Black45(content = "서울", color = black)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 179.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_train_grey),
                    contentDescription = "장소"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 281.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium11(content = "암스테르담 공항", color = neutral_600)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 297.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Black45(content = "군산", color = black)
            }

            Row(
                Modifier
                    .padding(bottom = 41.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Column(
                    Modifier.width(107.dp)
                ) {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                }
                Column(
                    Modifier.padding(start = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium11(content = "카드 갯수", color = neutral_500)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        Modifier
                            .size(60.dp)
                            .background(
                                color = white,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = black,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        YJ_Bold20(content = "27", color = primary_500)
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = 1.dp, end = 18.dp)
                    .align(Alignment.BottomEnd)
            ) {
                P_Medium11(content = "2024 . 02 . 15", color = neutral_500)
                P_Medium11(content = " / ", color = neutral_500)
                P_Medium11(content = "2024 . 02 . 15", color = neutral_500)
            }


            Row() {
                Column(
                    modifier = Modifier
                        .width(28.dp)
                        .fillMaxHeight()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_logo_vertical),
                        contentDescription = "로고",
                        Modifier
                            .width(22.dp)
                            .fillMaxHeight()
                    )
                }
            }

            Column(Modifier.padding(top = 400.dp)) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_circle),
                    contentDescription = "로고",
                    Modifier
                        .height(25.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun ReceiptPost_Big(){
        //모아보기에서 영수증 하나 선택해서 크게 보는 화면 (수정, 내보내기 가능)

        var intent = Intent(this@ReciptActivity, MainActivity::class.java)
        intent.putExtra("MoveScreen", "ReceiptPost")

        val chosenTheme  = "theme1"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { startActivity(intent) }) {
                    YJ_Bold15("뒤로", black)
                }
                Column() {
                    Row(){
                        Column(
                            Modifier
                                .padding(vertical = 10.dp, horizontal = 14.dp)
                                .clickable { }) {
                            YJ_Bold15("내보내기", black)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            Modifier
                                .padding(vertical = 10.dp, horizontal = 14.dp)
                                .clickable {
                                    navController.navigate(ReciptScreen.EditReceipt.name)
                                    }) {
                            YJ_Bold15("수정", black)
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 앞에서 불러온 화면 데이터 받아와서 수정안되는 버전으로 넣기
            if (chosenTheme == "theme1") {
                SaveTheme1()
            }
            if(chosenTheme == "theme2"){
                SaveTheme2()

            }
        }

    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun EditReceipt(){
        val page = 2
        val state = rememberPagerState()

        val viewModel: CustomNoTitleCheckViewModel = viewModel()
        val CustomNoTitleCheckDialogState = viewModel.CustomNoTitleCheckDialogState.value

        val receiptIntent = Intent(this@ReciptActivity, MainActivity::class.java)
        receiptIntent.putExtra("MoveScreen", "ReceiptPost")

        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }

        val emotionList = mutableStateListOf<Emotion>()

        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = "60%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = "20%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = "15%"
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요 ",
                persent = "5%"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { viewModel.showCustomNoTitleCheckDialog() }) {
                    YJ_Bold15("뒤로", black)

                    if (CustomNoTitleCheckDialogState.description.isNotBlank()){
                        CustomNoTitleCheckDialog(
                            description = CustomNoTitleCheckDialogState.description,
                            checkleft = CustomNoTitleCheckDialogState.checkleft,
                            checkright = CustomNoTitleCheckDialogState.checkright,
                            onClickleft = { startActivity( receiptIntent ) },
                            onClickright = {CustomNoTitleCheckDialogState.onClickright()},
                            onClickCancel = {CustomNoTitleCheckDialogState.onClickCancel()},

                            )
                    }
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { navController.navigate(ReciptScreen.SaveEditReceipt.name) }) {
                    YJ_Bold15("저장", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Horizontal_Theme(page,state,intro, depart_small, depart, arrive_small, arrive, emotionList)
        }
    }
    @Composable
    fun SaveEditReceipt(chosenTheme: String){

        val chosenTheme  = "theme1"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable { }) {
                    YJ_Bold15("내보내기", black)
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable {
                            val intent = Intent(
                                this@ReciptActivity,
                                MainActivity::class.java
                            )
                            intent.putExtra("MoveScreen","ReceiptPost")
                            startActivity( intent )
                        }) {
                    YJ_Bold15("완료", primary_500)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 앞에서 불러온 화면 데이터 받아와서 수정안되는 버전으로 넣기
            if (chosenTheme == "theme1") {
                SaveTheme1()
            }
            if(chosenTheme == "theme2"){
                SaveTheme2()

            }
        }
    }


    @Preview(apiLevel = 33)
    @Composable
    fun ReciptPreview() {
        ApplicationTheme {
            SaveRecipt("theme1")
        }
    }}


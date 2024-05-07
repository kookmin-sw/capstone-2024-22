package com.capstone.android.application.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.app.composable.CustomNoTitleCheckDialog
import com.capstone.android.application.data.local.Emotion
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.domain.CustomNoTitleCheckViewModel
import com.capstone.android.application.domain.ReceiptTrip
import com.capstone.android.application.domain.Trip
import com.capstone.android.application.presentation.ReceiptViewModel
import com.capstone.android.application.presentation.TripViewModel
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
import com.capstone.android.application.ui.theme.neutral_200
import com.capstone.android.application.ui.theme.neutral_300
import com.capstone.android.application.ui.theme.neutral_400
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
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate


enum class ReciptScreen(){
    MakeTripChoice,
    MakeTrip,
    SaveRecipt,
    ReceiptPost_Big,
    EditReceipt,
    SaveEditReceipt
}

class ReciptActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val receiptViewModel : ReceiptViewModel by viewModels()
    private val tripViewModel : TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            val movenav = intent.getStringExtra("MoveScreen")


            //여행 부르기
            tripViewModel.getTripAll()
            val tripList = remember { mutableStateListOf<ReceiptTrip>()}
            tripViewModel.getTripAllSuccess.observe(this@ReciptActivity){ response->
                response.data.trips.mapNotNull { trip -> runCatching {
                    ReceiptTrip(id=trip.id,tripName = trip.tripName, startDate = trip.startDate, endDate = trip.endDate, analyzingCount = trip.analyzingCount) }
                    .onSuccess { tripList.clear() }
                    .onFailure {}.getOrNull()
                }.forEach {
                    tripList.add(it)
                }
            }
            tripViewModel.getTripAllFailure.observe(this@ReciptActivity){ response->
                Log.d("tripListtripList", "fail")
            }



            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {

                }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    startDestination =
                    if(movenav == "ReceiptPost_Big") ReciptScreen.ReceiptPost_Big.name
                    else ReciptScreen.MakeTripChoice.name
                ) {
                    composable(route = ReciptScreen.MakeTripChoice.name) { MakeTripChoice() }
                    composable(route = ReciptScreen.MakeTrip.name) { MakeTrip() }
                    composable(route = ReciptScreen.SaveRecipt.name) {
                        val theme = remember {
                            navController.previousBackStackEntry?.savedStateHandle?.get<Int>("theme")
                        }
                        if (theme != null) {
                            SaveRecipt(theme)
                        }
                    }
                    composable(route = ReciptScreen.ReceiptPost_Big.name) {
                        //MainAvtivity 영수증모아보기에서 선택한 데이터로 크게 띄움
                        ReceiptPost_Big(/*receiptcontent*/) }
                    composable(route = ReciptScreen.EditReceipt.name) { EditReceipt () }
                    composable(route = ReciptScreen.SaveEditReceipt.name) {
                        val theme = remember {
                            navController.previousBackStackEntry?.savedStateHandle?.get<Int>("theme")
                        }
                        val receipt_content = remember {
                            navController.previousBackStackEntry?.savedStateHandle?.get<ReceiptContent>("receipt_content")
                        }
                        if (theme != null) {
                            if (receipt_content != null) {
                                SaveEditReceipt (theme,receipt_content)
                            }
                        }
                    }
                }
            }
        }
    }

    data class ReceiptContent(
        val tripName: String,
        val intro: MutableState<String>,
        val depart_small: MutableState<String>,
        val depart: MutableState<String>,
        val arrive_small: MutableState<String>,
        val arrive: MutableState<String>,
        val cardnum: Int,
        val publicationdate: String,
        val startdate: String,
        val enddate: String,
        val emotionList: SnapshotStateList<Emotion>
    )

    data class ReceiptContent_string(
        val tripName: String?,
        val intro: String?,
        val depart_small: String?,
        val depart: String?,
        val arrive_small: String?,
        val arrive: String?,
        val cardnum: Int?,
        val publicationdate: String?,
        val startdate: String?,
        val enddate: String?,
        val emotionList: SnapshotStateList<Emotion>
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MakeTripChoice(tripList:MutableList<ReceiptTrip>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
                    .background(color = tertiary_500)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 23.dp)
                        .wrapContentSize()
                ) {
                    ImgBackButton(onClick = {startActivity(Intent(this@ReciptActivity, MainActivity::class.java).putExtra("MoveScreen","Receipt"))}, "여행 선택하기")
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 0.dp)
                        .wrapContentSize()
                ) {

                    items(
                        count = tripList.size,
                        itemContent = {index->

                            if(tripList[index].analyzingCount==0 && isDatePassed(LocalDate.parse(tripList[index].endDate))){
                                ItemTrip(
                                    Trip(id=tripList[index].id,
                                        tripName = tripList[index].tripName,
                                        startDate=tripList[index].startDate,
                                        endDate = tripList[index].endDate),
                                    index = index)
                                Column(Modifier.padding(start = 16.dp, end = 9.dp)) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Divider(color = neutral_300)
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ItemTrip(trip: Trip, index : Int) {
        Column(
            modifier = Modifier
                .background(color = tertiary_500)
                .clickable {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "ItemData",
                        value = index
                    )
                    navController.navigate(ReciptScreen.MakeTrip.name)
                },
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
                        P_Medium11(trip.startDate, black)
                        Spacer(modifier = Modifier.height(6.dp))
                        P_Medium11(trip.endDate, black)
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
                    P_ExtraBold14(trip.tripName, black)
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
    fun MakeTrip(){
        val page = 2
        val state = rememberPagerState()

        val viewModel: CustomNoTitleCheckViewModel = viewModel()
        val CustomNoTitleCheckDialogState = viewModel.CustomNoTitleCheckDialogState.value

        val receiptIntent = Intent(this@ReciptActivity, MainActivity::class.java)
        receiptIntent.putExtra("MoveScreen", "Receipt")

        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }
        val cardnum = 27
        val publicationdate = "2024.02.25"
        val startdate = "2024.02.25"
        val enddate = "2024.02.27"
        val emotionList = mutableStateListOf<Emotion>()

        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = 60
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = 20
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = 15
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요",
                persent = 5
            )
        )
        emotionList.sortByDescending { it.persent }
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
                }

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
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "theme",
                                value = state.currentPage
                            )
                            navController.navigate(ReciptScreen.SaveRecipt.name)
                        }) {
                    YJ_Bold15("완료", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Horizontal_Theme(page,state,ReceiptContent(intro, depart_small, depart, arrive_small, arrive,
                cardnum,publicationdate,startdate,enddate, emotionList))
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Horizontal_Theme(
        page: Int,
        state: PagerState,
        receiptcontent: ReceiptContent
    ) {

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
                    EditTripTheme1(receiptcontent)
                }
            }
            if (page == 1) {
                Column(Modifier.padding(horizontal = 20.dp)){
                    EditTripTheme2(receiptcontent)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        DotsIndicator(totalDots = page, selectedIndex = state.currentPage)
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun EditTripTheme1(
        receiptcontent: ReceiptContent
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
                        " 티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다." +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다." +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다." +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다.", primary_200
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReciptTextField(
                        hint = "여행의 기록을 한 줄로 기록하세요:)",
                        onValueChanged = { receiptcontent.intro.value = it },
                        text = receiptcontent.intro,
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
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ReciptTextField(
                    hint = "기억 속에 오래 저장할",
                    onValueChanged = {receiptcontent.depart_small.value = it },
                    text = receiptcontent.depart_small,
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
                    hint = "출발지",
                    onValueChanged = {receiptcontent.depart.value = it },
                    text = receiptcontent.depart,
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
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ReciptTextField(
                    hint = "기억 속에 오래 저장할",
                    onValueChanged = { receiptcontent.arrive_small.value = it },
                    text = receiptcontent.arrive_small,
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
                    hint = "도착지",
                    onValueChanged = {receiptcontent.arrive.value = it },
                    text = receiptcontent.arrive,
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
                    P_ExtraBold14(content =receiptcontent.cardnum.toString(), color = primary_500)
                    Spacer(modifier = Modifier.height(30.dp))
                    P_Medium11(content = "여행 날짜", color = neutral_500)
                    Spacer(modifier = Modifier.height(6.dp))
                    P_Medium11(content = receiptcontent.startdate, color = primary_500)
                    Spacer(modifier = Modifier.height(5.dp))
                    P_Medium11(content = receiptcontent.enddate, color = primary_500)
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
                            receiptcontent.emotionList.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier.height(25.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.width(109.dp)
                                    )  {
                                        LinearProgressIndicator(
                                            progress = { item.persent.toFloat()/100 },
                                            modifier = Modifier.height(8.dp),
                                            color = when(index)
                                            {
                                                0 -> primary_500
                                                1 -> neutral_600
                                                2 -> neutral_400
                                                3 -> neutral_200
                                                else -> primary_500
                                            },
                                            strokeCap = StrokeCap.Round
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        modifier = Modifier.size(12.dp),
                                        painter = painterResource(id =Theme1_Emotion(item.text,index)),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    P_Medium11(content = item.persent.toString()+"%",
                                        color = when(index) {
                                        0 -> primary_500
                                        1 -> neutral_600
                                        2 -> neutral_400
                                        3 -> neutral_200
                                        else -> primary_500
                                    })
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
        receiptcontent: ReceiptContent
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
                    BorderStroke((0.5).dp, neutral_500),
                    shape = RoundedCornerShape(5.dp)
                )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 21.dp, start = 36.dp)
            ) {
                ReciptTextField(
                    hint = "여행의 기록을 한 줄로 기록하세요:)",
                    onValueChanged = { receiptcontent.intro.value = it },
                    text = receiptcontent.intro,
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
                    " 티켓이 발행된 날짜는"+ receiptcontent.publicationdate +"입니다. " +
                            "티켓이 발행된 날짜는"+ receiptcontent.publicationdate +"입니다. " +
                            "티켓이 발행된 날짜는"+ receiptcontent.publicationdate +"입니다. " +
                            "티켓이 발행된 날짜는"+ receiptcontent.publicationdate +"입니다. ", primary_200
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 484.dp, start = 28.dp, end = 10.dp),
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
                    .padding(end = 10.dp, start = 28.dp, top = 109.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ReciptTextField(
                    hint = "기억 속에 오래 저장할",
                    onValueChanged = { receiptcontent.depart_small.value = it },
                    text = receiptcontent.depart_small,
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
                    .padding(end = 10.dp, start = 28.dp, top = 133.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReciptTextField(
                    hint = "출발지",
                    onValueChanged = {receiptcontent.depart.value = it },
                    text = receiptcontent.depart,
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
                    .padding(end = 10.dp, start = 28.dp, top = 202.dp),
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
                    .padding(end = 10.dp, start = 28.dp, top = 312.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ReciptTextField(
                    hint = "기억 속에 오래 저장할",
                    onValueChanged = {receiptcontent.arrive_small.value = it },
                    text = receiptcontent.arrive_small,
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
                    .padding(end = 10.dp, start = 28.dp, top = 336.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReciptTextField(
                    hint = "도착지",
                    onValueChanged = {receiptcontent.arrive.value = it },
                    text = receiptcontent.arrive,
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier.size(14.dp),
                                    painter = painterResource(id = Theme2_Emotion(kind = item.text)),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(7.5.dp))
                                Column(
                                    modifier = Modifier.width(83.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { item.persent.toFloat()/100 },
                                        modifier = Modifier.height(8.dp),
                                        color = when(index)
                                        {
                                            0 -> primary_500
                                            1 -> neutral_600
                                            2 -> neutral_400
                                            3 -> neutral_200
                                            else -> primary_500
                                        },
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                    }
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
                        YJ_Bold20(content = receiptcontent.cardnum.toString(), color = primary_500)
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = 1.dp, end = 18.dp)
                    .align(Alignment.BottomEnd)
            ) {
                P_Medium11(content = receiptcontent.startdate, color = neutral_500)
                P_Medium11(content = " / ", color = neutral_500)
                P_Medium11(content = receiptcontent.enddate, color = neutral_500)
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

            Column(Modifier.padding(top = 453.dp)) {
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
    fun SaveRecipt(theme: Int){

        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }
        val cardnum = 27
        val publicationdate = "2024.02.25"
        val startdate = "2024.02.25"
        val enddate = "2024.02.27"

        intro.value = "한줄 소개 입니다"
        depart.value = "서울"
        depart_small.value = "서울숲"
        arrive.value = "부산"
        arrive_small.value = "해운대"

        val emotionList = mutableStateListOf<Emotion>()
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = 60
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = 20
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = 15
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요",
                persent = 5
            )
        )
        emotionList.sortByDescending { it.persent }
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

            if (theme == 0) {
                SaveTheme1(ReceiptContent(intro, depart_small, depart, arrive_small, arrive,
                    cardnum,publicationdate,startdate,enddate, emotionList))
            }
            if(theme == 1){
                SaveTheme2(ReceiptContent(intro, depart_small, depart, arrive_small, arrive,
                    cardnum,publicationdate,startdate,enddate, emotionList))

            }
        }
    }

    @Composable
    fun SaveTheme1(receiptcontent: ReceiptContent){
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
                        " 티켓이 발행된 날짜는" + receiptcontent.publicationdate+ "입니다. " +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate+ "입니다. " +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다. " +
                                "티켓이 발행된 날짜는" + receiptcontent.publicationdate +"입니다. ", primary_200
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium14(content = receiptcontent.intro.value, color = neutral_500)
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
                P_Medium14(content = receiptcontent.depart_small.value, color = primary_500)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 156.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_Black50(receiptcontent.depart.value, primary_500)
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
                P_Medium14(content = receiptcontent.arrive_small.value, color = primary_500)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 333.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_Black50(content = receiptcontent.arrive.value, color = primary_500)
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
                    P_ExtraBold14(content = receiptcontent.cardnum.toString(), color = primary_500)
                    Spacer(modifier = Modifier.height(30.dp))
                    P_Medium11(content = "여행 날짜", color = neutral_500)
                    Spacer(modifier = Modifier.height(6.dp))
                    P_Medium11(content = receiptcontent.startdate, color = primary_500)
                    Spacer(modifier = Modifier.height(5.dp))
                    P_Medium11(content = receiptcontent.enddate, color = primary_500)
                }
                Spacer(modifier = Modifier.width(50.dp))
                Column() {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(25.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.width(109.dp)
                                )  {
                                    LinearProgressIndicator(
                                        progress = { item.persent.toFloat()/100 },
                                        modifier = Modifier.height(8.dp),
                                        color = when(index)
                                        {
                                            0 -> primary_500
                                            1 -> neutral_600
                                            2 -> neutral_400
                                            3 -> neutral_200
                                            else -> primary_500
                                        },
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Image(
                                    modifier = Modifier.size(12.dp),
                                    painter = painterResource(id =Theme1_Emotion(item.text,index)),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                P_Medium11(content = item.persent.toString()+"%", color = primary_500)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SaveTheme2(receiptcontent: ReceiptContent){
        Box(
            modifier = Modifier
                .height(651.dp)
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
                    .padding(top = 21.dp, start = 36.dp)
            ) {
                P_Medium14(content = receiptcontent.intro.value, color = neutral_500)
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
                    " 티켓이 발행된 날짜는"+receiptcontent.publicationdate+ "입니다. " +
                            "티켓이 발행된 날짜는"+receiptcontent.publicationdate+ "입니다. " +
                            "티켓이 발행된 날짜는"+receiptcontent.publicationdate+ "입니다. " +
                            "티켓이 발행된 날짜는"+receiptcontent.publicationdate+ "입니다. ", primary_200
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 484.dp, start = 28.dp, end = 10.dp),
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
                    .padding(end = 10.dp, start = 28.dp, top = 109.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium11(content = receiptcontent.depart_small.value, color =neutral_600)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 133.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Black45(content = receiptcontent.depart.value, color = black)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 202.dp),
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
                    .padding(end = 10.dp, start = 28.dp, top = 312.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location_grey),
                    contentDescription = "장소",
                    Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                P_Medium11(content = receiptcontent.arrive_small.value, color = neutral_600)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 336.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Black45(content = receiptcontent.arrive.value, color = black)
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier.size(14.dp),
                                    painter = painterResource(id = Theme2_Emotion(kind = item.text)),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(7.5.dp))
                                Column(
                                    modifier = Modifier.width(83.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { item.persent.toFloat()/100 },
                                        modifier = Modifier.height(8.dp),
                                        color = when(index)
                                        {
                                            0 -> primary_500
                                            1 -> neutral_600
                                            2 -> neutral_400
                                            3 -> neutral_200
                                            else -> primary_500
                                        },
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                    }
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
                        YJ_Bold20(content = receiptcontent.cardnum.toString(), color = primary_500)
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = 1.dp, end = 18.dp)
                    .align(Alignment.BottomEnd)
            ) {
                P_Medium11(content = receiptcontent.startdate, color = neutral_500)
                P_Medium11(content = " / ", color = neutral_500)
                P_Medium11(content = receiptcontent.enddate, color = neutral_500)
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

            Column(Modifier.padding(top = 453.dp)) {
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
        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }
        val cardnum = 27
        val publicationdate = "2024.02.25"
        val startdate = "2024.02.25"
        val enddate = "2024.02.27"
        val emotionList = mutableStateListOf<Emotion>()
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = 60
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = 20
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = 15
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요 ",
                persent = 5
            )
        )
        emotionList.sortByDescending { it.persent }
        val receiptcontent = ReceiptContent(
            intro, depart_small, depart, arrive_small, arrive,
            cardnum, publicationdate, startdate, enddate, emotionList
        )
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
                SaveTheme1(receiptcontent)
            }
            if(chosenTheme == "theme2"){
                SaveTheme2(receiptcontent)

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
        val cardnum = 27
        val publicationdate = "2024.02.25"
        val startdate = "2024.02.25"
        val enddate = "2024.02.27"
        val emotionList = mutableStateListOf<Emotion>()

        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = 60
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = 20
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = 15
            )
        )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요 ",
                persent = 5
            )
        )
        emotionList.sortByDescending { it.persent }
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
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "theme",
                                value = state.currentPage
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "receipt_content",
                                value = ReceiptContent(
                                    intro, depart_small, depart, arrive_small, arrive,
                                    cardnum, publicationdate, startdate, enddate, emotionList
                                )
                            )
                            navController.navigate(ReciptScreen.SaveEditReceipt.name)
                        }) {
                    YJ_Bold15("저장", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Horizontal_Theme(page,state,ReceiptContent(intro, depart_small, depart, arrive_small, arrive,
                cardnum, publicationdate, startdate, enddate,emotionList))
        }
    }
    @Composable
    fun SaveEditReceipt(theme :Int, receiptcontent: ReceiptContent){

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
                            intent.putExtra("MoveScreen", "ReceiptPost")
                            startActivity(intent)
                        }) {
                    YJ_Bold15("완료", primary_500)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 앞에서 불러온 화면 데이터 받아와서 수정안되는 버전으로 넣기
            if (theme == 0) {
                SaveTheme1(receiptcontent)
            }
            if(theme == 1){
                SaveTheme2(receiptcontent)

            }
        }
    }

    @Composable
    fun Theme1_Emotion(kind: String, index: Int): Int {

        when(kind){
            "평범해요" -> return when(index){
                0 -> R.drawable.ic_receipt1_emotion_common_1
                1 -> R.drawable.ic_receipt1_emotion_common_2
                2 -> R.drawable.ic_receipt1_emotion_common_3
                3 -> R.drawable.ic_receipt1_emotion_common_4
                else -> R.drawable.ic_receipt1_emotion_common_1
            }
            "화가나요" -> return when(index){
                0 -> R.drawable.ic_receipt1_emotion_angry_1
                1 -> R.drawable.ic_receipt1_emotion_angry_2
                2 -> R.drawable.ic_receipt1_emotion_angry_3
                3 -> R.drawable.ic_receipt1_emotion_angry_4
                else -> R.drawable.ic_receipt1_emotion_angry_1
            }
            "즐거워요" -> return when(index){
                0 -> R.drawable.ic_receipt1_emotion_happy_1
                1 -> R.drawable.ic_receipt1_emotion_happy_2
                2 -> R.drawable.ic_receipt1_emotion_happy_3
                3 -> R.drawable.ic_receipt1_emotion_happy_4
                else -> R.drawable.ic_receipt1_emotion_happy_1
            }
            "슬퍼요" -> return when(index){
                0 -> R.drawable.ic_receipt1_emotion_sad_1
                1 -> R.drawable.ic_receipt1_emotion_sad_2
                2 -> R.drawable.ic_receipt1_emotion_sad_3
                3 -> R.drawable.ic_receipt1_emotion_sad_4
                else -> R.drawable.ic_receipt1_emotion_sad_1
            }
            else -> return R.drawable.ic_emotion_angry
        }
    }
    @Composable
    fun Theme2_Emotion(kind: String): Int {

        return when(kind){
            "평범해요" -> R.drawable.ic_receipt2_emotion_common
            "화가나요" -> R.drawable.ic_receipt2_emotion_angry
            "즐거워요" -> R.drawable.ic_receipt2_emotion_happy
            "슬퍼요" -> R.drawable.ic_receipt2_emotions_sad
            else -> R.drawable.ic_receipt2_emotion_common
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Preview(apiLevel = 33)
    @Composable
    fun ReciptPreview() {
        ApplicationTheme {
//            SaveRecipt("theme1")
        }
    }}


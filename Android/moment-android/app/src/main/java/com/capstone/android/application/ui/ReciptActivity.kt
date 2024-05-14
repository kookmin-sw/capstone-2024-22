package com.capstone.android.application.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.app.composable.CustomNoCheckDialog
import com.capstone.android.application.app.composable.CustomNoTitleCheckDialog
import com.capstone.android.application.data.local.Emotion
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_put.PutReceiptCreateRequest
import com.capstone.android.application.domain.CustomNoCheckViewModel
import com.capstone.android.application.domain.CustomNoTitleCheckViewModel
import com.capstone.android.application.domain.ReceiptAll
import com.capstone.android.application.domain.ReceiptTrip
import com.capstone.android.application.domain.Trip
import com.capstone.android.application.domain.TripDetail
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
import com.capstone.android.application.ui.theme.P_SemiBold18
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate


enum class ReciptScreen(){
    MakeTripChoice,
    MakeTrip,
    SaveRecipt,
    ReceiptPost_Big,
    EditReceipt,
    SaveEditReceipt,
    Loading
}
@AndroidEntryPoint
class ReciptActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val receiptViewModel : ReceiptViewModel by viewModels()
    private val tripViewModel : TripViewModel by viewModels()

    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = intent

        setContent {
            navController = rememberNavController()

            val movenav = try {
                intent.getStringExtra("MoveScreen")
            }catch (e : Exception){
                "Basic"
            }
            val tripDetailList = remember { mutableStateListOf<TripDetail>() }
            val receiptendDialogState = remember{ mutableStateOf(true) }
            //여행 부르기
            tripViewModel.getTripAll()
            val tripList = remember { mutableStateListOf<ReceiptTrip>()}
            tripViewModel.getTripAllSuccess.observe(this@ReciptActivity){ response->
                response.data.trips.mapNotNull { trip -> runCatching {
                    ReceiptTrip(id=trip.id,tripName = trip.tripName, startDate = trip.startDate,
                        endDate = trip.endDate, analyzingCount = trip.analyzingCount, numOfCard = trip.numOfCard) }
                    .onSuccess { tripList.clear() }
                    .onFailure {}.getOrNull()
                }.forEach {
                    tripList.add(it)
                }
            }
            tripViewModel.getTripAllFailure.observe(this@ReciptActivity){ response->
                Log.d("tripListtripList", "fail")
            }

            // 영수증 전체 받기 성공
            tripViewModel.getTripDetailSuccess.observe(this@ReciptActivity) { response ->
                Log.d("qwerqwerqwer", "success" +response.toString())
                tripDetailList.clear()

                tripDetailList.add(TripDetail(
                    tripName =response.data.tripName,
                    startDate =response.data.startDate,
                    endDate =response.data.endDate,
                    id =response.data.id,
                    happy =response.data.happy,
                    disgust =response.data.disgust,
                    angry =response.data.angry,
                    neutral =response.data.neutral,
                    sad =response.data.sad,
                    numOfCard =response.data.numOfCard,
                    analyzingCount =response.data.analyzingCount
                ))

                Log.d("qwerqwerqwer", "tripDetailList: ${tripDetailList[0]}")
            }

            // 영수증 전체 받기 실패
            tripViewModel.getTripDetailFailure.observe(this@ReciptActivity) { response ->
                Log.d("qwerqwerqwer", response.toString())
                setResult(1,mainIntent)
            }


            // 영수증 생성 성공
            receiptViewModel.postReceiptCreateSuccess.observe(this@ReciptActivity) { response ->
                Log.d("receiptViewModel_postReceiptCreateSuccess", response.toString())
            }
            // 영수증 생성 실패
            receiptViewModel.postReceiptCreateFailure.observe(this@ReciptActivity) { response ->
                Log.d("receiptViewModel_postReceiptCreateFailure", response.toString())
            }


            // 영수증 전체 받기 성공
            receiptViewModel.putReceiptCreateSuccess.observe(this@ReciptActivity) { response ->
                Log.d("putReceiptCreateSuccess", "success" + response.toString())
            }
            // 영수증 전체 받기 실패
            receiptViewModel.putReceiptCreateFailure.observe(this@ReciptActivity) { response ->
                Log.d("putReceiptCreateFailure", response.toString())
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
                    composable(route = ReciptScreen.MakeTripChoice.name) {
                        MakeTripChoice(tripList, mainIntent) }
                    composable(route = ReciptScreen.MakeTrip.name){

                        if(tripDetailList.size == 1) {
                            MakeTrip(tripDetailList[0],receiptendDialogState)
                        }
                    }
                    composable(route = ReciptScreen.SaveRecipt.name +
                            "/{tripName}/{intro.value}/{depart_small.value}/{depart.value}" +
                            "/{arrive_small.value}/{arrive.value}/{cardnum}" +
                            "/{publicationdate}/{startdate}/{enddate}/{theme}/{happy}/{sad}/{neutral}/{angry}/{disgust}",
                        arguments = listOf(
                            navArgument("tripName"){  defaultValue = "defaultValue" },
                            navArgument("intro"){  defaultValue = "" },
                            navArgument("depart_small"){  defaultValue = "" },
                            navArgument("depart"){  defaultValue = "defaultValue" },
                            navArgument("arrive_small"){  defaultValue = "" },
                            navArgument("arrive"){  defaultValue = "defaultValue" },
                            navArgument("cardnum"){  defaultValue = 1 },
                            navArgument("publicationdate"){  defaultValue = "defaultValue" },
                            navArgument("startdate"){  defaultValue = "defaultValue" },
                            navArgument("enddate"){  defaultValue = "defaultValue" },
                            navArgument("theme"){  defaultValue = "A" },
                            navArgument("happy"){  defaultValue = "0.0" },
                            navArgument("sad"){  defaultValue = "0.0" },
                            navArgument("neutral"){  defaultValue = "0.0" },
                            navArgument("angry"){  defaultValue = "0.0" },
                            navArgument("disgust"){  defaultValue = "0.0" }
                        )) { navBackStackEntry ->
                        val tripName = navBackStackEntry.arguments?.getString("tripName")
                        val intro = navBackStackEntry.arguments?.getString("intro.value")?:" "
                        val depart_small = navBackStackEntry.arguments?.getString("depart_small.value")?:" "
                        val depart = navBackStackEntry.arguments?.getString("depart.value")
                        val arrive_small = navBackStackEntry.arguments?.getString("arrive_small.value")?:" "
                        val arrive = navBackStackEntry.arguments?.getString("arrive.value")
                        val cardnum = navBackStackEntry.arguments?.getInt("cardnum")
                        val publicationdate = navBackStackEntry.arguments?.getString("publicationdate")
                        val startdate = navBackStackEntry.arguments?.getString("startdate")
                        val enddate = navBackStackEntry.arguments?.getString("enddate")
                        val theme = navBackStackEntry.arguments?.getString("theme")?:"A"
                        val happy = navBackStackEntry.arguments?.getString("happy")?:0.0
                        val sad = navBackStackEntry.arguments?.getString("sad")?:0.0
                        val neutral = navBackStackEntry.arguments?.getString("neutral")?:0.0
                        val angry = navBackStackEntry.arguments?.getString("angry")?:0.0
                        val disgust = navBackStackEntry.arguments?.getString("disgust")?:0.0

                        val emotionList = emotionPercent(
                            happy.toString().toDouble(),
                            sad.toString().toDouble(),
                            neutral.toString().toDouble(),
                            angry.toString().toDouble(),
                            disgust.toString().toDouble(), theme)


                        if (theme != null) {
                            SaveRecipt(theme,ReceiptContent_string(
                                tripName, intro, depart_small, depart, arrive_small, arrive,
                                cardnum,publicationdate,startdate,enddate, emotionList),receiptendDialogState
                            )
                        }
                    }
                    composable(route = ReciptScreen.ReceiptPost_Big.name) {

                        val data = intent.getSerializableExtra("BigReceipt") as ReceiptAll
                        val emotionList = emotionPercent(data.neutral/100,data.happy/100,data.angry/100,data.sad/100, data.disgust/100, data.receiptThemeType)
                        val created = data.created.take(10)
                        val receiptcontent = ReceiptContent_string(data.tripName,data.oneLineMemo, data.subDeparture,data.mainDeparture,
                            data.subDestination,data.mainDestination,data.numOfCard,created,data.stDate,data.edDate,emotionList)
                        ReceiptPost_Big(mainIntent, receiptcontent, data.receiptThemeType,data.neutral,data.happy,data.angry,data.sad, data.disgust, data.id)
                    }
                    composable(route = ReciptScreen.EditReceipt.name +
                            "/{tripName}/{intro}/{depart_small}/{depart}" +
                            "/{arrive_small}/{arrive}/{cardnum}" +
                            "/{publicationdate}/{startdate}/{enddate}/{happy}/{sad}/{neutral}/{angry}/{disgust}/{receiptid}",
                        arguments = listOf(
                            navArgument("tripName"){  defaultValue = "defaultValue" },
                            navArgument("intro"){  defaultValue = "" },
                            navArgument("depart_small"){  defaultValue = "" },
                            navArgument("depart"){  defaultValue = "defaultValue" },
                            navArgument("arrive_small"){  defaultValue = "" },
                            navArgument("arrive"){  defaultValue = "defaultValue" },
                            navArgument("cardnum"){  defaultValue = 1 },
                            navArgument("publicationdate"){  defaultValue = "defaultValue" },
                            navArgument("startdate"){  defaultValue = "defaultValue" },
                            navArgument("enddate"){  defaultValue = "defaultValue" },
                            navArgument("happy"){  defaultValue = "0.0" },
                            navArgument("sad"){  defaultValue = "0.0" },
                            navArgument("neutral"){  defaultValue = "0.0" },
                            navArgument("angry"){  defaultValue = "0.0" },
                            navArgument("disgust"){  defaultValue = "0.0" },
                            navArgument("receiptid"){  defaultValue = "0" }
                        )) { navBackStackEntry ->
                        val tripname = navBackStackEntry.arguments?.getString("tripName")?:" "
                        var intro = navBackStackEntry.arguments?.getString("intro")?:" "
                        var depart_small = navBackStackEntry.arguments?.getString("depart_small")?:" "
                        val depart = navBackStackEntry.arguments?.getString("depart")?:" "
                        var arrive_small = navBackStackEntry.arguments?.getString("arrive_small")?:" "
                        val arrive = navBackStackEntry.arguments?.getString("arrive")?:" "
                        val cardnum = navBackStackEntry.arguments?.getInt("cardnum")?:0
                        val publicationdate = navBackStackEntry.arguments?.getString("publicationdate")?:" "
                        val startdate = navBackStackEntry.arguments?.getString("startdate")?:" "
                        val enddate = navBackStackEntry.arguments?.getString("enddate")?:" "
                        val happy = navBackStackEntry.arguments?.getString("happy")?:0.0
                        val sad = navBackStackEntry.arguments?.getString("sad")?:0.0
                        val neutral = navBackStackEntry.arguments?.getString("neutral")?:0.0
                        val angry = navBackStackEntry.arguments?.getString("angry")?:0.0
                        val disgust = navBackStackEntry.arguments?.getString("disgust")?:0.0
                        val receiptid = navBackStackEntry.arguments?.getString("receiptid")?:0

                        val emotionList = emotionPercent(
                            happy.toString().toDouble()/100,
                            sad.toString().toDouble()/100,
                            neutral.toString().toDouble()/100,
                            angry.toString().toDouble()/100,
                            disgust.toString().toDouble()/100, "notheme")

                        val Intro = mutableStateOf("")
                        val Depart_small = mutableStateOf("")
                        val Depart = mutableStateOf("")
                        val Arrive_small = mutableStateOf("")
                        val Arrive = mutableStateOf("")
                        if(intro.trim().isEmpty()) intro = intro.trim()
                        if(depart_small.trim().isEmpty()) depart_small = depart_small.trim()
                        if(arrive_small.trim().isEmpty()) arrive_small = arrive_small.trim()

                        Intro.value = intro
                        Depart_small.value = depart_small
                        Depart.value = depart
                        Arrive_small.value = arrive_small
                        Arrive.value = arrive


                        val data = ReceiptContent(
                            tripname, Intro, Depart_small, Depart, Arrive_small, Arrive,
                            cardnum,publicationdate,startdate,enddate, emotionList)
                        EditReceipt(data,neutral.toString().toDouble()/100,happy.toString().toDouble()/100,
                            sad.toString().toDouble()/100,angry.toString().toDouble()/100,
                            disgust.toString().toDouble()/100 ,receiptid.toString().toInt(), mainIntent)
                    }
                    composable(route = ReciptScreen.SaveEditReceipt.name  +
                            "/{tripName}/{intro.value}/{depart_small.value}/{depart.value}" +
                            "/{arrive_small.value}/{arrive.value}/{cardnum}" +
                            "/{publicationdate}/{startdate}/{enddate}/{theme}/{happy}/{sad}/{neutral}/{angry}/{disgust}",
                        arguments = listOf(
                            navArgument("tripName"){  defaultValue = "defaultValue" },
                            navArgument("intro"){  defaultValue = "" },
                            navArgument("depart_small"){  defaultValue = "" },
                            navArgument("depart"){  defaultValue = "defaultValue" },
                            navArgument("arrive_small"){  defaultValue = "" },
                            navArgument("arrive"){  defaultValue = "defaultValue" },
                            navArgument("cardnum"){  defaultValue = 1 },
                            navArgument("publicationdate"){  defaultValue = "defaultValue" },
                            navArgument("startdate"){  defaultValue = "defaultValue" },
                            navArgument("enddate"){  defaultValue = "defaultValue" },
                            navArgument("theme"){  defaultValue = "A" },
                            navArgument("happy"){  defaultValue = "0.0" },
                            navArgument("sad"){  defaultValue = "0.0" },
                            navArgument("neutral"){  defaultValue = "0.0" },
                            navArgument("angry"){  defaultValue = "0.0" },
                            navArgument("disgust"){  defaultValue = "0.0" }
                        )) { navBackStackEntry ->
                        val tripName = navBackStackEntry.arguments?.getString("tripName")
                        val intro = navBackStackEntry.arguments?.getString("intro.value")?:" "
                        val depart_small = navBackStackEntry.arguments?.getString("depart_small.value")?:" "
                        val depart = navBackStackEntry.arguments?.getString("depart.value")
                        val arrive_small = navBackStackEntry.arguments?.getString("arrive_small.value")?:" "
                        val arrive = navBackStackEntry.arguments?.getString("arrive.value")
                        val cardnum = navBackStackEntry.arguments?.getInt("cardnum")
                        val publicationdate = navBackStackEntry.arguments?.getString("publicationdate")
                        val startdate = navBackStackEntry.arguments?.getString("startdate")
                        val enddate = navBackStackEntry.arguments?.getString("enddate")
                        val theme = navBackStackEntry.arguments?.getString("theme")?:"A"
                        val happy = navBackStackEntry.arguments?.getString("happy")?:0.0
                        val sad = navBackStackEntry.arguments?.getString("sad")?:0.0
                        val neutral = navBackStackEntry.arguments?.getString("neutral")?:0.0
                        val angry = navBackStackEntry.arguments?.getString("angry")?:0.0
                        val disgust = navBackStackEntry.arguments?.getString("disgust")?:0.0

                        val emotionList = emotionPercent(
                            happy.toString().toDouble(),
                            sad.toString().toDouble(),
                            neutral.toString().toDouble(),
                            angry.toString().toDouble(),
                            disgust.toString().toDouble(), theme)

                        SaveEditReceipt(theme,ReceiptContent_string(
                            tripName, intro, depart_small, depart, arrive_small, arrive,
                            cardnum,publicationdate,startdate,enddate, emotionList),mainIntent)
                    }
                    composable(route = ReciptScreen.Loading.name){
                        Loading()
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
        var emotionList: SnapshotStateList<Emotion>
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
    fun MakeTripChoice(tripList: MutableList<ReceiptTrip>, mainIntent: Intent)
    {
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
                Row(
                    modifier = Modifier
                        .padding(top = 23.dp)
                        .wrapContentSize()
                ) {
                    ImgBackButton(
                        onClick = {
                            setResult(1, mainIntent)
                            finish()
                        }, "")
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    P_SemiBold18(content = "여행 선택하기", color = black)
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

                            if(tripList[index].analyzingCount==0 &&
                                isDatePassed(LocalDate.parse(tripList[index].endDate)) &&
                                tripList[index].numOfCard != 0 ){
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
        BackHandler { finish() }
        Column(
            modifier = Modifier
                .background(color = tertiary_500)
                .clickable {
                    tripViewModel.getTripDetail(tripId = trip.id)
                    navController.navigate(ReciptScreen.Loading.name)
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
    @Composable
    fun Loading() {
        CircularProgress()
        Handler(Looper.getMainLooper()).postDelayed({

            navController.navigate(ReciptScreen.MakeTrip.name)
        }, 700)
    }
    @Composable
    fun CircularProgress() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary_500)
        }
    }


    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun MakeTrip(trip: TripDetail,receiptendDialogState : MutableState<Boolean>){
        val viewModel: CustomNoTitleCheckViewModel = viewModel()
        BackHandler { viewModel.showCustomNoTitleCheckDialog() }

        val page = 2
        val state = rememberPagerState()

        val CustomNoTitleCheckDialogState = viewModel.CustomNoTitleCheckDialogState.value

        val receiptIntent = Intent(this@ReciptActivity, MainActivity::class.java)
        receiptIntent.putExtra("MoveScreen", "Receipt")

        val intro = remember { mutableStateOf("") }
        val depart_small = remember { mutableStateOf("") }
        val depart = remember { mutableStateOf("") }
        val arrive_small = remember { mutableStateOf("") }
        val arrive = remember { mutableStateOf("") }

        val publicationdate = getCurrentDate().year.toString() + " . " + getCurrentDate().monthValue.toString() +" . " + getCurrentDate().dayOfMonth.toString()
        val tripName = trip.tripName
        val startdate = trip.startDate
        val enddate = trip.endDate
        val tripid = trip.id
        val cardnum = trip.numOfCard
        var theme = if (state.currentPage == 0) "A" else "B"
        var emotionList = emotionPercent(trip.neutral, trip.happy, trip.angry, trip.sad, trip.disgust, theme)

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
                        onClickleft = { finish() },
                        onClickright = {CustomNoTitleCheckDialogState.onClickright()},
                        onClickCancel = {CustomNoTitleCheckDialogState.onClickCancel()},

                        )
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable() {
                            receiptendDialogState.value = true
                            if (arrive.value == "" || depart.value == "") {
                                showToastMessage(
                                    context = this@ReciptActivity,
                                    "출발지와 도착지는 꼭 입력해주세요!"
                                )
                            } else {

                                if (intro.value == "") intro.value = " "
                                if (depart_small.value == "") depart_small.value = " "
                                if (arrive_small.value == "") arrive_small.value = " "

                                receiptViewModel.postReceiptCreate(
                                    body = PostReceiptCreateRequest(
                                        mainDeparture = depart.value,
                                        mainDestination = arrive.value,
                                        oneLineMemo = intro.value,
                                        receiptThemeType = if (state.currentPage == 0) "A" else "B",
                                        subDeparture = depart_small.value,
                                        subDestination = arrive_small.value,
                                        tripId = tripid
                                    )
                                )

                                val receiptData = ReceiptContent(
                                    tripName, intro, depart_small, depart, arrive_small, arrive,
                                    cardnum, publicationdate, startdate, enddate, emotionList
                                )
                                if (intro.value == "") intro.value = " "
                                if (depart_small.value == "") depart_small.value = " "
                                if (arrive_small.value == "") arrive_small.value = " "

                                navController.navigate(
                                    ReciptScreen.SaveRecipt.name +
                                            "/${receiptData.tripName}" +
                                            "/${receiptData.intro.value}" +
                                            "/${receiptData.depart_small.value}" +
                                            "/${receiptData.depart.value}" +
                                            "/${receiptData.arrive_small.value}" +
                                            "/${receiptData.arrive.value}" +
                                            "/${receiptData.cardnum}" +
                                            "/${receiptData.publicationdate}" +
                                            "/${receiptData.startdate}" +
                                            "/${receiptData.enddate}" +
                                            "/${theme}" +
                                            "/${trip.happy}" +
                                            "/${trip.sad}" +
                                            "/${trip.neutral}" +
                                            "/${trip.angry}"+
                                            "/${trip.disgust}"
                                )
                            }
                        }) {
                    YJ_Bold15("저장", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                Horizontal_Theme(page,state,ReceiptContent(tripName, intro, depart_small, depart, arrive_small, arrive,
                    cardnum,publicationdate,startdate,enddate, emotionList))
            }
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
        val intro = remember { mutableStateOf(receiptcontent.intro.value) }
        val depart_small = remember { mutableStateOf(receiptcontent.depart_small.value) }
        val depart = remember { mutableStateOf(receiptcontent.depart.value) }
        val arrive_small = remember { mutableStateOf(receiptcontent.arrive_small.value) }
        val arrive = remember { mutableStateOf(receiptcontent.arrive.value) }

        receiptcontent.intro.value = intro.value
        receiptcontent.depart_small.value = depart_small.value
        receiptcontent.depart.value = depart.value
        receiptcontent.arrive_small.value = arrive_small.value
        receiptcontent.arrive.value = arrive.value


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
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    P_Medium14(content = receiptcontent.tripName, color = white)
                    Image(
                        painter = painterResource(R.drawable.img_logo_white),
                        contentDescription = "로고 화이트"
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
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                ReciptTextField(
                    hint = "기억 속에 오래 저장할",
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
                    hint = "출발지",
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
                Image(
                    painter = painterResource(R.drawable.ic_location_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                receiptcontent.arrive_small?.let {
                    ReciptTextField(
                        hint = "기억 속에 오래 저장할",
                        onValueChanged = { arrive_small.value = it },
                        text = it,
                        keyboardType = KeyboardType.Text,
                        textcolor = primary_500,
                        fontweight = FontWeight.Medium,
                        fontsize = 14.sp,
                        type = "small"
                    )
                }
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
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .height(150.dp),
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
                Spacer(modifier = Modifier.width(30.dp))
                Column() {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.wrapContentWidth()
                    ) {

                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(16.dp),
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
                                            4 -> neutral_100
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
                                        4 -> neutral_100
                                        else -> primary_500
                                    })
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
        val intro = remember { mutableStateOf(receiptcontent.intro.value) }
        val depart_small = remember { mutableStateOf(receiptcontent.depart_small.value) }
        val depart = remember { mutableStateOf(receiptcontent.depart.value) }
        val arrive_small = remember { mutableStateOf(receiptcontent.arrive_small.value) }
        val arrive = remember { mutableStateOf(receiptcontent.arrive.value) }

        receiptcontent.intro.value = intro.value
        receiptcontent.depart_small.value = depart_small.value
        receiptcontent.depart.value = depart.value
        receiptcontent.arrive_small.value = arrive_small.value
        receiptcontent.arrive.value = arrive.value

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
                receiptcontent.intro?.let {
                    ReciptTextField(
                        hint = "여행의 기록을 한 줄로 기록하세요:)",
                        onValueChanged = { intro.value = it },
                        text = it,
                        keyboardType = KeyboardType.Text,
                        textcolor = neutral_500,
                        fontweight = FontWeight.Medium,
                        fontsize = 14.sp,
                        type = "intro"
                    )
                }
            }

            Column(Modifier.padding(top = 51.dp)) {
                Divider(color = primary_500, thickness = 2.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 470.dp, start = 28.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                P_Medium14(content = receiptcontent.tripName, color = black)
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
                receiptcontent.depart_small?.let {
                    ReciptTextField(
                        hint = "기억 속에 오래 저장할",
                        onValueChanged = { depart_small.value = it },
                        text = it,
                        keyboardType = KeyboardType.Text,
                        textcolor = neutral_600,
                        fontweight = FontWeight.Medium,
                        fontsize = 11.sp,
                        type = "small1"
                    )
                }
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
                    onValueChanged = {depart.value = it },
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
                receiptcontent.arrive_small?.let {
                    ReciptTextField(
                        hint = "기억 속에 오래 저장할",
                        onValueChanged = {arrive_small.value = it },
                        text = it,
                        keyboardType = KeyboardType.Text,
                        textcolor = neutral_600,
                        fontweight = FontWeight.Medium,
                        fontsize = 11.sp,
                        type = "small1"
                    )
                }
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
                    onValueChanged = {arrive.value = it },
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
                    .wrapContentWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {

                Column(
                    Modifier.width(107.dp)
                ) {
                    Row(modifier = Modifier.width(107.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                        P_Medium11(content = " ", color = neutral_500)
                        P_Medium11(content = "여행 감정", color = neutral_500)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(15.dp),
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
                                        modifier = Modifier.height(4.dp),
                                        color = when(index)
                                        {
                                            0 -> primary_500
                                            1 -> neutral_600
                                            2 -> neutral_400
                                            3 -> neutral_200
                                            4 -> neutral_100
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
                    .padding(bottom = 6.dp, end = 18.dp)
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

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun SaveRecipt(theme: String, receiptcontent: ReceiptContent_string, receiptendDialogState: MutableState<Boolean>){

        BackHandler {
            startActivity(Intent(this@ReciptActivity, MainActivity::class.java)
                .putExtra("MoveScreen", "ReceiptPost"))
            finish()
        }

        var Theme = 0
        Theme = if (theme == "A") 0 else 1

        val coroutineScope = rememberCoroutineScope()
        val context = androidx.compose.ui.platform.LocalContext.current

        val picture = CaptureComposableAsImage {
            if (Theme == 0) {
                SaveTheme1(receiptcontent)
            }
            if(Theme == 1){
                SaveTheme2(receiptcontent)
            }
        }


        Handler(Looper.getMainLooper()).postDelayed({
            receiptendDialogState.value = false
        }, 3500)
        val viewModel: CustomNoCheckViewModel = viewModel()
        val CustomNoCheckDialogState = viewModel.CustomNoCheckDialogState.value

        if(receiptendDialogState.value){
            //다이얼로그 띄우기
            viewModel.showCustomNoCheckDialog()
            if (CustomNoCheckDialogState.description.isNotBlank()){
                CustomNoCheckDialog(
                    title = "축하해요!",
                    description = "영수증이 만들어졌어요\n" + "나도 이제 여행자 !",
                    num = 1
                    )
            }
        }else{

        }

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
                        .clickable {
                            coroutineScope.launch {
                                val capturedImageBitmap = createBitmapFromPicture(picture).asImageBitmap()

                                ShareImage(capturedImageBitmap.toBitmap(),context)
                            }
                        }) {
                    YJ_Bold15("내보내기", black)
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable {
                            startActivity(Intent(this@ReciptActivity, MainActivity::class.java)
                                .putExtra("MoveScreen", "ReceiptPost"))
                            finish()
                        }) {
                    YJ_Bold15("완료", primary_500)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (Theme == 0) {
                    SaveTheme1(receiptcontent)
                }
                if(Theme == 1){
                    SaveTheme2(receiptcontent)
                }
            }
        }
    }

    @Composable
    fun SaveTheme1(receiptcontent: ReceiptContent_string){
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
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    receiptcontent.tripName?.let { P_Medium14(content = it, color = white)
                    }?: P_Medium14(content = "", color = white)
                    Image(
                        painter = painterResource(R.drawable.img_logo_white),
                        contentDescription = "로고 화이트"
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    receiptcontent.intro?.let { P_Medium14(content = it, color = neutral_500)
                    }?: P_Medium14(content = "", color = white)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 136.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if(receiptcontent.depart_small == " " || receiptcontent.depart_small == ""){
                    Spacer(modifier = Modifier.size(19.dp))
                }else{
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                receiptcontent.depart_small?.let { P_Medium14(content = it, color = primary_500) }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 156.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                receiptcontent.depart?.let { P_Black50(it, primary_500) }
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
                if(receiptcontent.arrive_small == " " || receiptcontent.arrive_small == ""){
                    Spacer(modifier = Modifier.size(19.dp))
                }else{
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                receiptcontent.arrive_small?.let { P_Medium14(content = it, color = primary_500)
                }?: P_Medium14(content = "", color = white)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 333.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                receiptcontent.arrive?.let { P_Black50(content = it, color = primary_500)
                }?: P_Medium14(content = "", color = white)
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
                    .wrapContentWidth()
                    .height(150.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    Modifier
                        .height(150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium11(content = "여행 카드", color = neutral_500)
                    Spacer(modifier = Modifier.height(10.dp))
                    P_ExtraBold14(content = receiptcontent.cardnum.toString(), color = primary_500)
                    Spacer(modifier = Modifier.height(30.dp))
                    P_Medium11(content = "여행 날짜", color = neutral_500)
                    Spacer(modifier = Modifier.height(6.dp))
                    receiptcontent.startdate?.let { P_Medium11(content = it, color = primary_500)
                    }?: P_Medium14(content = "", color = white)
                    Spacer(modifier = Modifier.height(5.dp))
                    receiptcontent.enddate?.let { P_Medium11(content = it, color = primary_500)
                    }?: P_Medium14(content = "", color = white)
                }
                Spacer(modifier = Modifier.width(30.dp))
                Column() {
                    P_Medium11(content = "여행 감정", color = neutral_500)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(16.dp),
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
                                            4 -> neutral_100
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
                                    color = when(index)
                                    {
                                        0 -> primary_500
                                        1 -> neutral_600
                                        2 -> neutral_400
                                        3 -> neutral_200
                                        4 -> neutral_100
                                        else -> primary_500
                                    })
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SaveTheme2(receiptcontent: ReceiptContent_string){
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
                receiptcontent.intro?.let { P_Medium14(content = it, color = neutral_500)
                }?: P_Medium14(content = "", color = white)
            }

            Column(Modifier.padding(top = 51.dp)) {
                Divider(color = primary_500, thickness = 2.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 470.dp, start = 28.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                receiptcontent.tripName?.let { P_Medium14(content = it, color = black)
                }?: P_Medium14(content = "", color = white)
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
                if(receiptcontent.depart_small == " " || receiptcontent.depart_small == ""){
                    Spacer(modifier = Modifier.size(19.dp))
                }else{
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                receiptcontent.depart_small?.let { P_Medium11(content = it, color =neutral_600)
                }?: P_Medium14(content = "", color = white)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 133.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                receiptcontent.depart?.let { P_Black45(content = it, color = black)
                }?: P_Medium14(content = "", color = white)
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
                if(receiptcontent.arrive_small == " " || receiptcontent.arrive_small == ""){
                    Spacer(modifier = Modifier.size(19.dp))
                }else{
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                receiptcontent.arrive_small?.let { P_Medium11(content = it, color = neutral_600)
                }?: P_Medium14(content = "", color = white)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 28.dp, top = 336.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                receiptcontent.arrive?.let { P_Black45(content = it, color = black)
                }?: P_Medium14(content = "", color = white)
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
                            .wrapContentWidth()
                    ) {
                        receiptcontent.emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.height(15.dp),
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
                                        modifier = Modifier.height(4.dp),
                                        color = when(index)
                                        {
                                            0 -> primary_500
                                            1 -> neutral_600
                                            2 -> neutral_400
                                            3 -> neutral_200
                                            4 -> neutral_100
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
                    .padding(bottom = 6.dp, end = 18.dp)
                    .align(Alignment.BottomEnd)
            ) {
                receiptcontent.startdate?.let { P_Medium11(content = "$it / ", color = neutral_500)
                }?: P_Medium14(content = "", color = white)
                receiptcontent.enddate?.let { P_Medium11(content = it, color = neutral_500)
                }?: P_Medium14(content = "", color = white)
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

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun ReceiptPost_Big(mainIntent: Intent, content: ReceiptContent_string, theme : String,
                        neutral:Double ,happy:Double ,angry:Double ,sad:Double, disgust:Double,receiptid : Int){

        val tripName = content.tripName
        val intro = if(content.intro?.isEmpty() == true) " "  else content.intro
        val depart_small = if(content.depart_small?.isEmpty() == true) " "  else content.depart_small
        val depart = content.depart
        val arrive_small = if(content.arrive_small?.isEmpty() == true) " "  else content.arrive_small
        val arrive = content.arrive
        val cardnum = content.cardnum
        val publicationdate = content.publicationdate
        val startdate = content.startdate
        val enddate = content.enddate
        val emotionList = content.emotionList

        val receiptcontent = ReceiptContent_string(
            tripName, intro, depart_small, depart, arrive_small, arrive,
            cardnum, publicationdate, startdate, enddate, emotionList
        )

        val coroutineScope = rememberCoroutineScope()
        val context = androidx.compose.ui.platform.LocalContext.current

        val picture = CaptureComposableAsImage {
            if (theme == "A") {
                SaveTheme1(receiptcontent)
            }
            if (theme == "B") {
                SaveTheme2(receiptcontent)

            }
        }
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
                        .clickable {
                            setResult(1, mainIntent)
                            finish()
                        }) {
                    YJ_Bold15("뒤로", black)
                }
                Column() {
                    Row(){
                        Column(
                            Modifier
                                .padding(vertical = 10.dp, horizontal = 14.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        val capturedImageBitmap =
                                            createBitmapFromPicture(picture).asImageBitmap()

                                        ShareImage(capturedImageBitmap.toBitmap(), context)
                                    }
                                }) {
                            YJ_Bold15("내보내기", black)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            Modifier
                                .padding(vertical = 10.dp, horizontal = 14.dp)
                                .clickable {
                                    navController.navigate(
                                        ReciptScreen.EditReceipt.name +
                                                "/${tripName}" +
                                                "/${intro}" +
                                                "/${depart_small}" +
                                                "/${depart}" +
                                                "/${arrive_small}" +
                                                "/${arrive}" +
                                                "/${cardnum}" +
                                                "/${publicationdate}" +
                                                "/${startdate}" +
                                                "/${enddate}" +
                                                "/${happy}" +
                                                "/${sad}" +
                                                "/${neutral}" +
                                                "/${angry}" +
                                                "/${disgust}" +
                                                "/${receiptid}"
                                    )
                                }) {
                            YJ_Bold15("수정", black)
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                if (theme == "A") {
                    SaveTheme1(receiptcontent)
                }
                if (theme == "B") {
                    SaveTheme2(receiptcontent)
                }
            }
        }

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun EditReceipt(receiptContent: ReceiptContent,
                    neutral:Double ,happy:Double ,angry:Double ,sad:Double, disgust:Double ,receiptid : Int, mainIntent :Intent){

        val viewModel: CustomNoTitleCheckViewModel = viewModel()
        BackHandler {
            viewModel.showCustomNoTitleCheckDialog()
        }
        val page = 2
        val state = rememberPagerState()

        val CustomNoTitleCheckDialogState = viewModel.CustomNoTitleCheckDialogState.value

        val receiptIntent = Intent(this@ReciptActivity, MainActivity::class.java)
        receiptIntent.putExtra("MoveScreen", "ReceiptPost")

        receiptContent.emotionList = emotionPercent(neutral,happy,angry,sad, disgust, if(state.currentPage == 0) "A" else "B")
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
                            onClickleft = {
                                //영수증 모아보기로 이동
                                setResult(1, mainIntent)
                                finish() },
                            onClickright = {CustomNoTitleCheckDialogState.onClickright()},
                            onClickCancel = {CustomNoTitleCheckDialogState.onClickCancel()},

                            )
                    }
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable() {

                            if (receiptContent.arrive.value == "" || receiptContent.depart.value == "") {
                                showToastMessage(
                                    context = this@ReciptActivity,
                                    "출발지와 도착지는 꼭 입력해주세요!"
                                )
                            } else {

                                receiptViewModel.putReceiptCreate(body =
                                PutReceiptCreateRequest(
                                    mainDeparture = receiptContent.depart.value,
                                    mainDestination =  receiptContent.arrive.value,
                                    oneLineMemo =  receiptContent.intro.value,
                                    receiptThemeType = if (state.currentPage == 0) "A" else "B",
                                    subDeparture =  receiptContent.depart_small.value,
                                    subDestination =  receiptContent.arrive_small.value,
                                    id =  receiptid
                                    )
                                )

                                var theme = "A"
                                if (receiptContent.intro.value == "") receiptContent.intro.value =
                                    " "
                                if (receiptContent.depart_small.value == "") receiptContent.depart_small.value =
                                    " "
                                if (receiptContent.arrive_small.value == "") receiptContent.arrive_small.value =
                                    " "
                                theme = if (state.currentPage == 0) "A" else "B"

                                navController.navigate(
                                    ReciptScreen.SaveEditReceipt.name +
                                            "/${receiptContent.tripName}" +
                                            "/${receiptContent.intro.value}" +
                                            "/${receiptContent.depart_small.value}" +
                                            "/${receiptContent.depart.value}" +
                                            "/${receiptContent.arrive_small.value}" +
                                            "/${receiptContent.arrive.value}" +
                                            "/${receiptContent.cardnum}" +
                                            "/${receiptContent.publicationdate}" +
                                            "/${receiptContent.startdate}" +
                                            "/${receiptContent.enddate}" +
                                            "/${theme}" +
                                            "/${happy}" +
                                            "/${sad}" +
                                            "/${neutral}" +
                                            "/${angry}"+
                                            "/${disgust}"
                                )
                            }
                        }) {
                    YJ_Bold15("저장", black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
            Horizontal_Theme(page,state,receiptContent)
            }
        }
    }


    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun SaveEditReceipt(theme: String, receiptcontent: ReceiptContent_string, mainIntent: Intent){

        BackHandler {
            //영수증 모아보기로 이동
            setResult(1, mainIntent)
            finish()
        }


        var Theme = 0
        Theme = if (theme == "A") 0 else 1

        val coroutineScope = rememberCoroutineScope()
        val context = androidx.compose.ui.platform.LocalContext.current

        val picture = CaptureComposableAsImage {
            if (Theme == 0) {
                SaveTheme1(receiptcontent)
            }
            if(Theme == 1){
                SaveTheme2(receiptcontent)
            }
        }

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
                        .clickable {
                            coroutineScope.launch {
                                val capturedImageBitmap = createBitmapFromPicture(picture).asImageBitmap()

                                ShareImage(capturedImageBitmap.toBitmap(),context)
                            }
                        }) {
                    YJ_Bold15("내보내기", black)
                }
                Column(
                    Modifier
                        .padding(vertical = 10.dp, horizontal = 14.dp)
                        .clickable {
                            //영수증 모아보기로 이동
                            setResult(1, mainIntent)
                            finish()
                        }) {
                    YJ_Bold15("완료", primary_500)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (Theme == 0) {
                    SaveTheme1(receiptcontent)
                }
                if (Theme == 1) {
                    SaveTheme2(receiptcontent)
                }
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
            "불쾌해요" -> return when(index){
                0 -> R.drawable.ic_receipt1_emotion_disgust1
                1 -> R.drawable.ic_receipt1_emotion_disgust2
                2 -> R.drawable.ic_receipt1_emotion_disgust3
                3 -> R.drawable.ic_receipt1_emotion_disgust4
                else -> R.drawable.ic_receipt1_emotion_disgust5
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
            "슬퍼요" -> R.drawable.ic_receipt2_emotion_sad
            "불쾌해요" -> R.drawable.ic_receipt2_emotion_disgust
            else -> R.drawable.ic_receipt2_emotion_common
        }
    }



    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isDatePassed(targetDate: LocalDate): Boolean {
        val currentDate = getCurrentDate()
        return currentDate.isAfter(targetDate) || currentDate.isEqual(targetDate)
    }

    fun emotionPercent(common : Double, happy : Double, angry : Double, sad : Double, disgust :Double, theme : String): SnapshotStateList<Emotion> {
        val emotionList = mutableStateListOf<Emotion>()

        if(theme == "A") {
            emotionList.clear()
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "평범해요", persent = (common*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_happy, text = "즐거워요", persent =(happy*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_angry, text = "화가나요", persent = (angry*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_sad, text = "슬퍼요", persent = (sad *100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "불쾌해요", persent = (disgust*100).toInt()))
            emotionList.sortByDescending { it.persent }
        }
        if(theme == "B") {
            emotionList.clear()
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_angry, text = "화가나요", persent = (angry*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_happy, text = "즐거워요", persent =(happy*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "평범해요", persent = (common*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_sad, text = "슬퍼요", persent = (sad *100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "불쾌해요", persent = (disgust*100).toInt()))
        }
        else {
            emotionList.clear()
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "평범해요", persent = (common*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_happy, text = "즐거워요", persent =(happy*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_angry, text = "화가나요", persent = (angry*100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_sad, text = "슬퍼요", persent = (sad *100).toInt()))
            emotionList.add( Emotion(icon = R.drawable.ic_emotion_common, text = "불쾌해요", persent = (disgust*100).toInt()))
            emotionList.sortByDescending { it.persent } }
        return emotionList
    }



    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun CaptureComposableAsImage(content: @Composable () -> Unit): Picture {
        val picture = remember { Picture() }
        Column {
            // Content to be captured
            Box(
                modifier = Modifier
                    .drawWithCache {
                        val width = this.size.width.toInt()
                        val height = this.size.height.toInt()

                        onDrawWithContent {
                            val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(width, height))
                            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()

                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    }
            ) {
                content()
            }
        }
        return picture
    }


    private fun createBitmapFromPicture(picture: Picture): Bitmap {
        val width = picture.width
        val height = picture.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        picture.draw(canvas)
        return bitmap
    }
    fun ImageBitmap.toBitmap(): Bitmap {
        return this.asAndroidBitmap()
    }
    fun ShareImage(bitmap: Bitmap, context: Context) {


        // 이미지를 저장할 파일 생성
        val cachePath = File(context.cacheDir, "images")
        if (!cachePath.exists()) {
            cachePath.mkdirs()
        }
        val imageFile = File(cachePath, "shared_image.png")

        // 이미지를 파일로 저장
        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        // 파일 URI 생성
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
        // 이미지를 공유하는 Intent 생성
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // 이미지를 공유하기 위한 Intent 실행
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))

    }



    @SuppressLint("UnrememberedMutableState")
    @Preview(apiLevel = 33)
    @Composable
    fun ReciptPreview() {
        ApplicationTheme {
//            SaveRecipt("theme1")
        }
    }}


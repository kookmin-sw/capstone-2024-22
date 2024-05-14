package com.capstone.android.application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.app.ApplicationClass.Companion.tokenSharedPreferences
import com.capstone.android.application.app.composable.CustomTitleCheckDialog
import com.capstone.android.application.app.composable.FancyProgressBar
import com.capstone.android.application.app.composable.TripEmpty
import com.capstone.android.application.app.composable.TripExist
import com.capstone.android.application.app.composable.TripIng
import com.capstone.android.application.app.composable.convertUrlLinkStringToRcorderNameString
import com.capstone.android.application.app.composable.getCurrentTime
import com.capstone.android.application.app.composable.getDifferenceInDay
import com.capstone.android.application.app.screen.BottomNavItem
import com.capstone.android.application.app.screen.MainScreen
import com.capstone.android.application.app.utile.MomentPath
import com.capstone.android.application.app.utile.common.GetWeatherType
import com.capstone.android.application.app.utile.firebase.MyFirebaseMessagingService
import com.capstone.android.application.app.utile.location.MomentLocation
import com.capstone.android.application.app.utile.permission.MomentPermission
import com.capstone.android.application.app.utile.recorder.MomentAudioPlayer
import com.capstone.android.application.app.utile.recorder.MomentAudioRecorder
import com.capstone.android.application.data.remote.card.model.card_post.request.PostCardUploadReqeust
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.ReceiptId
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.deleteReceiptDeleteRequest
import com.capstone.android.application.domain.Card
import com.capstone.android.application.domain.ReceiptAll
import com.capstone.android.application.domain.Emotion
import com.capstone.android.application.domain.Trip
import com.capstone.android.application.domain.TripFile
import com.capstone.android.application.presentation.CardViewModel
import com.capstone.android.application.presentation.KakaoViewModel
import com.capstone.android.application.presentation.OpenWeatherViewModel
import com.capstone.android.application.presentation.ReceiptViewModel
import com.capstone.android.application.presentation.TripFileViewModel
import com.capstone.android.application.presentation.TripViewModel
import com.capstone.android.application.ui.CardActivity
import com.capstone.android.application.ui.PatchTripActivity
import com.capstone.android.application.ui.PostTripActivity
import com.capstone.android.application.ui.ReciptActivity
import com.capstone.android.application.ui.SplashActivity
import com.capstone.android.application.ui.TripFileActivity
import com.capstone.android.application.ui.theme.ApplicationTheme
import com.capstone.android.application.ui.theme.BigButton
import com.capstone.android.application.ui.theme.FontMoment
import com.capstone.android.application.ui.theme.P_ExtraBold
import com.capstone.android.application.ui.theme.P_Medium
import com.capstone.android.application.ui.theme.P_Medium11
import com.capstone.android.application.ui.theme.P_Medium14
import com.capstone.android.application.ui.theme.P_Medium18
import com.capstone.android.application.ui.theme.P_Medium_Oneline
import com.capstone.android.application.ui.theme.YJ_Bold
import com.capstone.android.application.ui.theme.YJ_Bold15
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_100
import com.capstone.android.application.ui.theme.neutral_200
import com.capstone.android.application.ui.theme.neutral_300
import com.capstone.android.application.ui.theme.neutral_400
import com.capstone.android.application.ui.theme.neutral_500
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.primary_200
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.secondary_50
import com.capstone.android.application.ui.theme.tertiary_500
import com.capstone.android.application.ui.theme.white
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt

enum class TripState{
    EMPTY,EXISTS,ING
}
data class MainTrip(
    var state:TripState = TripState.EMPTY,
    var tripName:String="",
    var period:Int=0
)

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val tripViewModel : TripViewModel by viewModels()
    private val cardViewModel : CardViewModel by viewModels()
    private val receiptViewModel : ReceiptViewModel by viewModels()
    private val tripFileViewModel : TripFileViewModel by viewModels()
    private val kakaoViewModel : KakaoViewModel by viewModels()
    private val openWeatherViewModel : OpenWeatherViewModel by viewModels()
    @Inject lateinit var momentLocation : MomentLocation
    @Inject lateinit var momentPermission: MomentPermission
    @Inject lateinit var recorder: MomentAudioRecorder
    @Inject lateinit var player: MomentAudioPlayer
    @Inject lateinit var momentAudioPlayer:MomentAudioPlayer
    private var audioFile: File? = null


    private fun MyFirebaseMessaging() {
        val token: Task<String> = FirebaseMessaging.getInstance().token
        token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCMToken",task.result)
//                registerFCMToken(task.result)
            }
        })
    }


    private fun registerFCMToken(FCMToken:String){
        val fcm = Intent(applicationContext, MyFirebaseMessagingService::class.java)
        startService(fcm)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentDate:String=""
        momentPermission.requestPermission()
        MyFirebaseMessaging()


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD hh:mm:ss A Z"))
//            currentDate = current
//        } else {
//            val date = Date(System.currentTimeMillis())
//            val dateFormat = SimpleDateFormat(
//                "yyyy-MM-dd HH:mm",
//                Locale.KOREA
//            )
//
//            currentDate=dateFormat.format(date)
//        }

        setContent {

            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                MainRoot()
            }
        }
    }

    @Composable
    fun MainRoot() {
        var movenav = "Basic"
         try {
             movenav = intent.getStringExtra("MoveScreen").toString()
             if (movenav == "ReceiptPost") {
                 receiptViewModel.getReceiptAll(0,10000)
                 Log.d("ReceiptPost", "MainRoot: ")
             }
        } catch (e: Exception) {
             movenav = "Basic"
        }

        val postTrip =
            rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == 1) {
//                    val intent = result.data
                    tripViewModel.getTripAll()
                    //do something here
                }

                if (result.resultCode == 2) {
//                    val intent = result.data
                    tripViewModel.getTripAll()
                    //do something here
                }
            }

        val makeReceipt =
            rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == 1) {
                    receiptViewModel.getReceiptAll(0,10000)
                }
            }

        val tripList = remember {
            mutableStateListOf<Trip>()
        }
        val tripFileUntitledList = remember {
            mutableStateListOf<TripFile>()
        }

        val lat = remember {
            mutableStateOf("")
        }

        val lon = remember {
            mutableStateOf("")
        }

        val temp = remember {
            mutableStateOf("")
        }

        val weather = remember {
            mutableStateOf("")
        }
        val addressName = remember {
            mutableStateOf("")
        }

        val favoriteCardList = remember {
            mutableStateListOf<Card>()
        }

        val DeleteReceipt = remember { mutableListOf<ReceiptId>() }
        val test: Int = 4
        val colorName: Result<String> = runCatching {
            when (test) {
                1 -> "파란색"
                2 -> "빨간색"
                3 -> "노란색"
                else -> throw Error("처음 들어보는 색")
            }
        }.onSuccess { it: String ->
            Log.d("awegweagewag", it)
        }.onFailure { it: Throwable ->}
            //실패시만 실행 (try - catch문의 catch와 유사)


            var mainTrip = remember {
                mutableStateOf(MainTrip())

            }


            val bottomItems = listOf<BottomNavItem>(
                BottomNavItem.Home,
                BottomNavItem.Receipt,
                BottomNavItem.Record,
                BottomNavItem.Favorite,
                BottomNavItem.Setting,
            )

            val scope = rememberCoroutineScope()
            val title = remember {
                mutableStateOf("추가")
            }

            val currentSelectedBottomRoute = remember {
                mutableStateOf("홈")
            }
            //Record Bottomsheet
            val sheetState = rememberModalBottomSheetState(/*
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {false}*/
            )

            val recordOpen = remember { mutableStateOf(false) }
            val isRecorderIng = remember { mutableStateOf(false) }
            var EditCheckState = remember { mutableStateOf(false) }
            var ReceiptCheckState = remember { mutableStateOf(true) }


        val coroutineScope = rememberCoroutineScope()
            val emotionList = remember {
                mutableStateListOf<Emotion>()
            }

            emotionList.add(
                Emotion(
                    icon = R.drawable.ic_emotion_common,
                    text = "평범해요",
                    persent = 0f,
                    color = "#99342E"
                )
            )
            emotionList.add(
                Emotion(
                    icon = R.drawable.ic_emotion_happy,
                    text = "즐거워요",
                    persent = 0f,
                    color = "#030712"
                )
            )
            emotionList.add(
                Emotion(
                    icon = R.drawable.ic_emotion_angry,
                    text = "화가나요",
                    persent = 0f,
                    color = "#DAD0B4"
                )
            )
            emotionList.add(
                Emotion(
                    icon = R.drawable.ic_emotion_sad,
                    text = "슬퍼요 ",
                    persent = 0f,
                    color = "#1F9854"
                )
            )
        emotionList.add(
            Emotion(
                icon = R.drawable.ic_receipt2_emotion_disgust,
                text = "불쾌해요 ",
                persent = 0f,
                color = "#1F9854"
            )
        )

            // 영수증 삭제 dialog
            val showDeleteDialog = remember { mutableStateOf(false) }
            val deleteyes = remember { mutableStateOf(false) }

            tripViewModel.getTripAll()
            cardViewModel.getCardLiked()
            tripFileViewModel.getTripFileUntitled()
            receiptViewModel.getReceiptAll(0,10000)


            tripViewModel.getTripAllSuccess.observe(this@MainActivity) { response ->
                tripList.clear()
                response.data.trips.mapNotNull { trip ->
                    runCatching {
                        Trip(
                            id = trip.id,
                            tripName = trip.tripName,
                            startDate = trip.startDate,
                            endDate = trip.endDate
                        )
                    }
                        .onSuccess {
                        }.onFailure {
                        }
                        .getOrNull()
                }.forEach {
                    tripList.add(it)
                }





                if (tripList.isNotEmpty()) {
                    Log.d("awegagwea", "waegewa")
                    tripList.sortedBy { it.startDate }
                    ApplicationClass.tripName = tripList.first().tripName
                    val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val currentDate = format.parse(getCurrentTime())
                    val compareDate = format.parse(tripList.first().startDate)

                    val different =
                        getDifferenceInDay(startDate = currentDate, endDate = compareDate)

                    run breaker@{
                        tripList.forEach {

                            if (currentDate.compareTo(format.parse(it.startDate)) < 0) {
                                mainTrip.value = MainTrip(
                                    state = TripState.EXISTS,
                                    tripName = it.tripName,
                                    period = abs(
                                        getDifferenceInDay(
                                            startDate = currentDate,
                                            endDate = format.parse(it.startDate)
                                        )
                                    )
                                )
                                ApplicationClass.tripName = it.tripName
                                return@breaker
                            } else if (currentDate.compareTo(format.parse(it.endDate)) < 0) {
                                mainTrip.value = MainTrip(
                                    state = TripState.ING,
                                    tripName = it.tripName,
                                    period = abs(
                                        getDifferenceInDay(
                                            startDate = currentDate,
                                            endDate = format.parse(it.startDate)
                                        )
                                    ) + 1
                                )
                                ApplicationClass.tripName = it.tripName
                                return@breaker
                            } else {
                                mainTrip.value = MainTrip(
                                    state = TripState.EMPTY,
                                    tripName = it.tripName,
                                    period = abs(
                                        getDifferenceInDay(
                                            startDate = currentDate,
                                            endDate = format.parse(it.startDate)
                                        )
                                    ) + 1
                                )
                            }
                        }
                    }

                } else {
                    mainTrip.value = MainTrip(
                        state = TripState.EMPTY
                    )
                    Log.d("awegwgwae", "wawegewa")
                    tripList.clear()
                }


            }

            tripViewModel.getTripAllFailure.observe(this@MainActivity) { response ->

            }

            tripViewModel.deleteTripSuccess.observe(this@MainActivity) { response ->
                runCatching {
                    when (response.status) {

                    }
                }
                tripViewModel.getTripAll()
            }

            cardViewModel.getCardLikedSuccess.observe(this@MainActivity) { response ->
                favoriteCardList.clear()
                response.data.cardViews.mapNotNull { cardView ->
                    kotlin.runCatching {
                        Card(
                            cardView = cardView,
                            uploadImage = ArrayList<File>(),
                            imageNum = mutableStateOf(0)
                        )
                    }.onSuccess {
                        favoriteCardList.clear()
                    }.onFailure {

                    }.getOrNull()
                }.forEach { card ->
                    favoriteCardList.add(card)
                }
            }

            cardViewModel.postCardUploadSuccess.observe(this@MainActivity) { response ->
                coroutineScope
                    .launch {
                        sheetState.hide()
                        recordOpen.value = false
                    }
            }

            cardViewModel.getCardLikedSuccess.observe(this@MainActivity) {

            }

            tripFileViewModel.getTripFileUntitledSuccess.observe(this@MainActivity) { response ->
                response.data.tripFiles.mapNotNull { tripFile ->
                    kotlin.runCatching {
                        TripFile(
                            id = tripFile.id,
                            tripId = tripFile.tripId,
                            yearDate = tripFile.yearDate,
                            analyzingCount = mutableStateOf(tripFile.totalCount)
                        )
                    }.onSuccess {
                        tripFileUntitledList.clear()
                    }.onFailure { }.getOrNull()
                }.forEach { it ->
                    tripFileUntitledList.add(it)
                }
            }

            kakaoViewModel.getLocalSuccess.observe(this@MainActivity) { response ->
                try {
                    addressName.value = response.documents.first().address_name


                } catch (e: Exception) {
                    Timber.e(e)
                }
            }


            // 영수증 삭제 성공
            receiptViewModel.deleteReceiptDeleteSuccess.observe(this@MainActivity) { response ->
                EditCheckState.value = false
                DeleteReceipt.clear()
                receiptViewModel.getReceiptAll(0, 10000)
            }
            // 영수증 삭제 실패
            receiptViewModel.deleteReceiptDeleteFailure.observe(this@MainActivity) { response ->
            }

            val receiptList = remember { mutableStateListOf<ReceiptAll>() }
            // 영수증 전체 받기 성공
            receiptViewModel.getReceiptAllSuccess.observe(this@MainActivity) { response ->
                    receiptList.clear()
                    response.data.receiptList.mapNotNull { receiptAll ->
                        runCatching {
                            ReceiptAll(
                                receiptAll.id,
                                receiptAll.tripId,
                                receiptAll.tripName,
                                receiptAll.angry,
                                receiptAll.disgust,
                                receiptAll.happy,
                                receiptAll.sad,
                                receiptAll.neutral,
                                receiptAll.stDate,
                                receiptAll.edDate,
                                receiptAll.numOfCard,
                                receiptAll.mainDeparture,
                                receiptAll.subDeparture,
                                receiptAll.mainDestination,
                                receiptAll.subDestination,
                                receiptAll.oneLineMemo,
                                receiptAll.receiptThemeType,
                                receiptAll.createdAt
                            )
                        }
                            .onSuccess { }
                            .onFailure {  }.getOrNull()
                    }.forEach {
                        receiptList.add(it)
                        /*if (response.data.receiptList.size == receiptList.size) {

                        } else {
                        }*/
                    }
            }
            // 영수증 전체 받기 실패
            receiptViewModel.getReceiptAllFailure.observe(this@MainActivity) { response ->
                Log.d("receiptViewModel_getReceiptAllFailure", response.toString())
            }

//        val recordOpen = remember { mutableStateOf(false)}
//        val EditCheckState = remember { mutableStateOf(false) }
//        val ReceiptCheckState = remember { mutableStateOf(true) }
//        val viewModel: CustomTitleCheckViewModel = viewModel()
//        val CustomTitleCheckDialogState = viewModel.CustomTitleCheckDialogState.value

            openWeatherViewModel.getWeatherInCurrentLocationlSuccess.observe(this@MainActivity) { response ->
                try {
                    temp.value = response.main.temp.toFloat().minus(273.15).toInt().toString()
                    weather.value = GetWeatherType(response.weather.first().main)

                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            openWeatherViewModel.getWeatherInCurrentLocationFailure.observe(this@MainActivity) { error ->


            }
            navController = rememberNavController()

            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp / 2)
                    .shadow(elevation = 6.dp),
                bottomBar = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavigation(
                            modifier = Modifier
                                .height(70.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            backgroundColor = Color.White,
                            elevation = 20.dp
                        ) {

                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            bottomItems.forEach { screen ->
                                BottomNavigationItem(
                                    selectedContentColor = Color.Black,
                                    unselectedContentColor = Color("#938F8F".toColorInt()),
                                    label = {
                                        Column(
                                            modifier = Modifier
                                                .height(35.dp)
                                        ) {
                                            Text(
                                                text = screen.label,
                                                fontSize = 12.sp
                                            )
                                        }
                                    },
                                    icon = {
                                        Column(
                                            modifier = Modifier.height(35.dp),
                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .height(6.dp)
                                            ) {
                                                Divider(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    thickness = (0.8).dp, color = Color.Black
                                                )
                                                if (screen?.screenRoute == currentSelectedBottomRoute.value) {
                                                    Divider(
                                                        modifier = Modifier
                                                            .width(36.dp)
                                                            .padding(top = (1.5).dp),
                                                        thickness = 4.dp,
                                                        color = Color("#99342E".toColorInt())
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            if (screen.selectedDrawableId != 0) {
                                                Image(
                                                    modifier = Modifier.size(20.dp),
                                                    painter =
                                                    if (screen?.screenRoute == currentSelectedBottomRoute.value) painterResource(
                                                        id = screen.selectedDrawableId
                                                    ) else painterResource(id = screen.unselectedDrawableId),
                                                    contentDescription = "search"
                                                )
                                            }
                                        }
                                    },
                                    selected = false,
                                    onClick = {
                                        if (screen != BottomNavItem.Record) {
                                            navController.navigate(screen.screenRoute) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }

                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true

                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .height(90.dp)
                                .clickable {
                                    recordOpen.value = true
                                    isRecorderIng.value = recorder.isActivity()

                                }
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = if (isRecorderIng.value) painterResource(id = R.drawable.ic_record_ing) else painterResource(
                                    id = R.drawable.ic_record_before
                                ),
                                contentDescription = ""
                            )
                        }
                    }
                },
                topBar = {
                    TopAppBar(
                        actions = {

                        },
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                navBackStackEntry.let {
                                    if (it == null) {
                                        Log.d("waegfwa", "null")
                                    } else {
                                        Log.d("waegfwa", it.destination.route.toString())

                                    }
                                }


                                navController.currentDestination.let {
                                    when (it?.route) {
                                        null -> " "
                                        "Home" -> " "
                                        "Receipt" -> " "
                                        "ReceiptPost" ->
                                            if (!EditCheckState.value) {
                                                Column(Modifier.clickable {
                                                    navController.navigate(
                                                        BottomNavItem.Receipt.screenRoute
                                                    ){
                                                        popUpTo(BottomNavItem.Receipt.screenRoute) { inclusive = true }
                                                    }
                                                }) {
                                                    Text(
                                                        text = "뒤로",
                                                        fontFamily = FontMoment.obangFont,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp
                                                    )
                                                }
                                            } else {
                                                Column(
                                                    Modifier
                                                        .wrapContentSize()
                                                        .clickable {
                                                            EditCheckState.value = false
                                                            DeleteReceipt.clear()
                                                        }) {
                                                    YJ_Bold15(content = "완료", color = black)
                                                }
                                            }

                                        "Favorite" -> " "
                                        "Setting" -> " "
                                        else -> null
                                    }
                                }

                                Spacer(Modifier.weight(1f))
                                Column() {
                                    Text(
                                        modifier = Modifier
                                            .clickable(enabled = if (title.value == "삭제" && !ReceiptCheckState.value) false else true) {
                                                when (title.value) {
                                                    "추가" -> {
                                                        postTrip.launch(
                                                            Intent(
                                                                this@MainActivity,
                                                                PostTripActivity::class.java
                                                            )
                                                        )
                                                    }

                                                    "영수증 모아보기" -> {
                                                        EditCheckState.value = false
                                                        DeleteReceipt.clear()
                                                        navController.navigate(MainScreen.ReceiptPost.screenRoute)
                                                        val size = 10000
                                                        receiptViewModel.getReceiptAll(0, size)
                                                    }

                                                    "작게보기" -> {}
                                                    "편집" -> {
                                                        EditCheckState.value = true
                                                    }

                                                    "삭제" -> {
                                                        if (DeleteReceipt.size != 0){
                                                            showDeleteDialog.value = true
                                                        }else{

                                                        }
                                                    }
                                                }
                                            },
                                        text = title.value,
                                        fontFamily = FontMoment.obangFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (title.value == "삭제") {
                                            if (ReceiptCheckState.value) primary_500 else neutral_500
                                        } else black
                                    )
                                }
                            }

                        }

                    )
                }
            ) { innerPadding ->
                if (showDeleteDialog.value){
                    //dialog 띄우기
                    deleteDialog(showDeleteDialog = showDeleteDialog,  DeleteReceipt = DeleteReceipt )
                }
                NavHost(
                    navController,
                    startDestination =
                    when (movenav) {
                        "Receipt" -> BottomNavItem.Receipt.screenRoute
                        "ReceiptPost" -> MainScreen.ReceiptPost.screenRoute
                        else -> BottomNavItem.Home.screenRoute
                    },
                    Modifier.padding(innerPadding)
                ) {
                    composable(BottomNavItem.Home.screenRoute) {
                        currentSelectedBottomRoute.value = "Home"
                        Log.d("waegwagewa", tripList.toString())
                        Home(tripList, mainTrip)
                        title.value = "추가"
                    }
                    composable(BottomNavItem.Receipt.screenRoute) {
                        title.value = "추가"
                        currentSelectedBottomRoute.value = "Receipt"

                        Receipt(makeReceipt)
                        title.value = "영수증 모아보기"
                    }
                    composable(BottomNavItem.Record.screenRoute) {
                        currentSelectedBottomRoute.value = "Record"

                        Record()

                    }
                    composable(BottomNavItem.Favorite.screenRoute) {
                        currentSelectedBottomRoute.value = "Favorite"
                        cardViewModel.getCardLiked()
                        Favorite(cardItems = favoriteCardList, emotionList = emotionList)
                        title.value = "작게보기"
                    }
                    composable(BottomNavItem.Setting.screenRoute) {
                        currentSelectedBottomRoute.value = "Setting"
                        Setting()
                        title.value = ""
                    }
                    composable(MainScreen.ReceiptPost.screenRoute) {
                        currentSelectedBottomRoute.value = MainScreen.ReceiptPost.rootRoute
                        ReceiptPost(makeReceipt, receiptList, EditCheckState, DeleteReceipt)
                        if (EditCheckState.value) title.value = "삭제" else title.value = "편집"
                    }

                    composable(MainScreen.HomeTrip.screenRoute) {
                        currentSelectedBottomRoute.value = MainScreen.HomeTrip.rootRoute
                        HomeTrip()
                    }
                    composable(MainScreen.RecordDaily.screenRoute) {
                        currentSelectedBottomRoute.value = MainScreen.RecordDaily.rootRoute

                        RecordDaily(tripFileUntitledList = tripFileUntitledList)
                    }
                }
                if (recordOpen.value) {
                    RecordNavigatesheet(
                        sheetState = sheetState,
                        closeSheet = { recordOpen.value = false },
                        lat = lat,
                        lon = lon,
                        temp = temp,
                        addressName = addressName,
                        weather = weather
                    )
                }
            }

    }

    enum class DragAnchors {
        Start,
        Center,
        End,
    }
    @OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
    @Composable
    fun Home(tripList:MutableList<Trip>,mainTrip:MutableState<MainTrip>){

        Log.d("waegawgaw",tripList.size.toString())
        val density = LocalDensity.current
        val defaultActionSize = 80.dp
        val endActionSizePx = with(density) { (defaultActionSize * 2).toPx() }
        val startActionSizePx = with(density) { defaultActionSize.toPx() }

        val state = remember {
            AnchoredDraggableState(
                initialValue = DragAnchors.Center,
                anchors = DraggableAnchors {
                    DragAnchors.Start at -startActionSizePx
                    DragAnchors.Center at 0f
                    DragAnchors.End at endActionSizePx
                },
                positionalThreshold = { distance: Float -> distance * 0.5f },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                animationSpec = tween(),
            )
        }

        val pagerState = rememberPagerState()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color("#706969".toColorInt()),
                thickness = 2.dp
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ){
                HorizontalPager(
                    state = pagerState
                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp) ,
                    verticalAlignment = Alignment.Top,
                    count = 2
                ){index ->
                    if(index == 0){
                        when(mainTrip.value.state){
                            TripState.EMPTY -> {
                                TripEmpty(text = "어디로 떠나면 좋을까요?")
                            }
                            TripState.ING -> {
                                TripIng(tripName = mainTrip.value.tripName, remainPeriod = mainTrip.value.period)
                            }
                            TripState.EXISTS -> {
                                TripExist(tripName = mainTrip.value.tripName, remainPeriod = mainTrip.value.period)
                            }
                        }
                    }else{
                        Text(
                            modifier = Modifier.clickable { navController.navigate(MainScreen.RecordDaily.screenRoute) },
                            text = "일상기록",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                LazyRow {
                    items(2){index->

                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(3.dp)
                                .background(
                                    color = if (pagerState.currentPage == index) Color.Black else Color(
                                        "#E7E6E6".toColorInt()
                                    ),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color("#706969".toColorInt()),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(40.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                thickness = 2.dp
            )
            
            if(tripList.size == 0){
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "아직 계획된 여행이 없나요?\n화면 상단에서 일정 추가가 가능해요",
                    color = neutral_300,
                    fontFamily = FontMoment.preStandardFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            }else{
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 4.dp)
                ){
                    items(
                        count = tripList.size,
                        itemContent = {index->
                            ItemTrip(type = 0,id=tripList[index].id, tripName = tripList[index].tripName,startDate=tripList[index].startDate, endDate = tripList[index].endDate)

//                        if(it==1){
//                            // '현재 여행중이에요' 알람
//                            ItemTrip(type = 1)
//                        }else if(it==2){
//                            // '카드 분석중이에요' 알람
//                            ItemTrip(type = 2)
//                        }else{
//                            ItemTrip(type = 0)
//
//                        }

                        }
                    )
                }
            }


        }
    }



    @Composable
    fun RecordDaily(tripFileUntitledList:MutableList<TripFile>){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color("#706969".toColorInt()),
                thickness = 2.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "일상기록",
                textAlign = TextAlign.End,
                fontSize = 22.sp,
                fontFamily = FontMoment.preStandardFont,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color("#706969".toColorInt()),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(28.dp))

            LazyColumn(
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
            ){
                items(
                    count = tripFileUntitledList.size,
                    itemContent = { index->
                        Column(
                            modifier = Modifier.clickable {
                                val intent = Intent(this@MainActivity,CardActivity::class.java)
                                intent.putExtra("tripFileId",tripFileUntitledList[index].id)
                                intent.putExtra("tripFileListIndex",tripFileUntitledList[index].id)

                                startActivity(intent)
                            } ,
                        ) {
                            Box(
                                modifier = Modifier.clip(RectangleShape),
                                contentAlignment = Alignment.CenterEnd
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .fillMaxSize()
                                        .background(color = Color.White),
                                    verticalAlignment = Alignment.CenterVertically
                                ){

                                    Text(
                                        text = tripFileUntitledList[index].yearDate,
                                        fontSize = 11.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Divider(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(1.dp)
                                        ,
                                        color = Color("#99342E".toColorInt()),
                                        thickness = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${tripFileUntitledList[index].analyzingCount.value}개의 파일이 있어요",
                                        fontSize = 11.sp,
                                        color = Color("#706969".toColorInt())
                                    )

                                }


                            }
                            Spacer(modifier = Modifier.height(24.dp))

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(end = 8.dp)
                                ,
                                color = Color("#C3C1C1".toColorInt()),
                                thickness = 2.dp
                            )

                        }
                    }
                )
            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemTrip(type:Int,id:Int,tripName:String,startDate:String,endDate:String){
        val density = LocalDensity.current
        val defaultActionSize = 80.dp
        val endActionSizePx = with(density) { (defaultActionSize * 2).toPx() }
        val startActionSizePx = with(density) { defaultActionSize.toPx() }

        val state = remember {
            AnchoredDraggableState(
                initialValue = DragAnchors.Start,
                anchors = DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at endActionSizePx
                },
                positionalThreshold = { distance: Float -> distance * 0.5f },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                animationSpec = tween(),
            )
        }
//        Box(
//            contentAlignment = Alignment.TopCenter
//        ){
//            Image(
//                painter = painterResource(id = R.drawable.img_alarm_red), contentDescription = ""
//            )
//            Text(text = "weageawg")
//        }

        Column(
            modifier = Modifier.clickable {
                val intent = Intent(this@MainActivity,TripFileActivity::class.java)
                intent.putExtra("tripId",id)
                startActivity(intent)
            } ,
        ) {
            Box(
                modifier = Modifier.clip(RectangleShape),
                contentAlignment = Alignment.CenterEnd
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxSize()
                        .background(color = Color.White)
                        .offset {
                            IntOffset(
                                x = -state
                                    .requireOffset()
                                    .roundToInt(),
                                y = 0,
                            )
                        }
                        .anchoredDraggable(
                            state = state,
                            Orientation.Horizontal,
                            reverseDirection = true
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Column {
                        Text(
                            text = startDate,
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                        Text(
                            text = endDate,
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Divider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                        ,
                        color = Color("#99342E".toColorInt()),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = tripName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Divider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp),
                        color = Color("#99342E".toColorInt()),
                        thickness = 2.dp
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(end = 24.dp, top = 24.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                        .offset {
                            IntOffset(
                                (-state
                                    .requireOffset() + endActionSizePx)
                                    .roundToInt(), 0
                            )
                        } ,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            val intent = Intent(this@MainActivity, PatchTripActivity::class.java)
                            intent.putExtra("tripId",id)
                            intent.putExtra("startDate",startDate)
                            intent.putExtra("endDate",endDate)
                            intent.putExtra("tripName",tripName)
                            startActivity(intent)
                        },
                        text = "수정",
                        fontFamily = FontMoment.obangFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(26.dp))
                    Divider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color("#E7E6E6".toColorInt()),
                    )
                    Spacer(modifier = Modifier.width(26.dp))
                    Text(
                        modifier = Modifier.clickable {
                            tripViewModel.deleteTrip(
                                tripId = id
                            )
                        },
                        text = "삭제",
                        fontFamily = FontMoment.obangFont,
                        fontWeight = FontWeight.Bold,
                        color = Color("#99342E".toColorInt()),
                        fontSize = 14.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset {
                            IntOffset(
                                x = -state
                                    .requireOffset()
                                    .roundToInt(),
                                y = 0,
                            )
                        }
                        .anchoredDraggable(
                            state = state,
                            Orientation.Horizontal,
                            reverseDirection = true
                        )
                ) {
                    if(type==1){
                        Box(
                            contentAlignment = Alignment.TopCenter
                        ){
                            Spacer(modifier = Modifier.width(18.dp))

                            Image(
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(32.dp),
                                painter = painterResource(id = R.drawable.img_alarm_red), contentDescription = ""
                            )
                            Text(
                                text = "현재 여행중이에요",
                                fontSize = 8.sp,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    if(type==2){
                        Box(
                            contentAlignment = Alignment.TopCenter
                        ){
                            Image(
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(32.dp),
                                painter = painterResource(id = R.drawable.img_alarm_green), contentDescription = ""
                            )
                            Text(
                                text = "카드 분석중이에요",
                                fontSize = 8.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                    }



                }



            }
            Spacer(modifier = Modifier.height(24.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(end = 8.dp)
                ,
                color = Color("#C3C1C1".toColorInt()),
                thickness = 2.dp
            )

        }
    }

    @Composable
    fun HomeTrip(){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .clickable {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                CardActivity::class.java
                            )
                        )
                    }
                    .padding(horizontal = 4.dp)
            ){
                items(3){
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .background(color = Color.Gray)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Receipt(makeReceipt :
                ManagedActivityResultLauncher<Intent, ActivityResult>
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
            ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.img_receipt_preview), contentDescription = "영수증미리보기"
            )
            Spacer(modifier = Modifier.height(55.dp))
            BigButton("만들기", true,
                onClick = { makeReceipt.launch(
                    Intent(
                        this@MainActivity,
                        ReciptActivity::class.java
                    )
                ) })
        }
    }

    @Composable
    fun ReceiptPost(makeReceipt : ManagedActivityResultLauncher<Intent, ActivityResult>,
                    receiptList: MutableList<ReceiptAll>,EditCheckState: MutableState<Boolean>, DeleteReceipt :MutableList<ReceiptId> ) {
        //영수증 게시
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)) {
            MyGrid(makeReceipt, receiptList, 2, EditCheckState, DeleteReceipt)

        }
    }
    @Composable
    fun MyGrid(
        makeReceipt : ManagedActivityResultLauncher<Intent, ActivityResult>,
        receiptList: MutableList<ReceiptAll>,
        columnSize: Int,
        EditCheckState: MutableState<Boolean>,
        DeleteReceipt: MutableList<ReceiptId>
    ){
        val rowsCount = 1 + (receiptList.size -1)/columnSize // row 개수
        BoxWithConstraints {
            val maxWidth = this.maxWidth

            LazyColumn {
                items(rowsCount) { rowIndex ->
                    val rangeStart = rowIndex*columnSize
                    var rangeEnd = rangeStart + columnSize -1
                    if (rangeEnd > receiptList.lastIndex) rangeEnd = receiptList.lastIndex // row로 표현될 list의 range를 계산, slice하여 row 생성
                    RowOfGrid(makeReceipt, receiptList.slice(rangeStart..rangeEnd), maxWidth/columnSize,
                        EditCheckState, DeleteReceipt)
                }
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun RowOfGrid(
        makeReceipt : ManagedActivityResultLauncher<Intent, ActivityResult>,
        rowList: List<ReceiptAll>,
        columnWidth: Dp,
        EditCheckState: MutableState<Boolean>,
        DeleteReceipt: MutableList<ReceiptId>
    ) {
        var intent = Intent(this@MainActivity, ReciptActivity::class.java)
        intent.putExtra("MoveScreen", "ReceiptPost_Big")

        LazyRow {
            items(rowList.size) { index ->

                val item = rowList[index]
                val checkState = rememberSaveable { mutableStateOf(false) }

                if (!EditCheckState.value) {
                    checkState.value = false
                }

                val receiptId =  ReceiptId(item.id)
                Box( modifier = Modifier
                    .width(columnWidth)
                    .height(224.dp)
                    .padding(horizontal = 25.dp, vertical = 8.dp)
                    .background(Color.Gray)
                    .clickable {
                        if (!EditCheckState.value) {

                            makeReceipt.launch(
                                Intent(
                                    this@MainActivity,
                                    ReciptActivity::class.java
                                ).putExtra("MoveScreen", "ReceiptPost_Big")
                                    .putExtra("BigReceipt", item)
                            )
                        } else {
                            if (checkState.value) {
                                checkState.value = false
                                if (DeleteReceipt.contains(receiptId)) {
                                    DeleteReceipt.remove(receiptId)
                                }

                            } else {
                                checkState.value = true
                                if (!DeleteReceipt.contains(receiptId)) {
                                    DeleteReceipt.add(receiptId)
                                }
                            }
                        }
                    }) {

                    if(item.receiptThemeType == "A") MiniTheme1(item)
                    else MiniTheme2(item)


                    if (EditCheckState.value){
                        Column(
                            Modifier
                                .padding(10.dp)
                                .align(Alignment.BottomEnd)) {
                            Column(
                                Modifier
                                    .size(32.dp)
                                    .background(
                                        if (!checkState.value) neutral_500 else white,
                                        shape = RoundedCornerShape(3.dp)
                                    )
                                    .border(
                                        0.5.dp, black,
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            ) {

                            }
                        }
                        if(checkState.value){
                            Column(
                                Modifier
                                    .padding(10.dp)
                                    .align(Alignment.BottomEnd)) {
                                Column(Modifier.size(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_receipt_check_red),
                                        contentDescription = "체크버튼",
                                        Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun Record(){


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun  RecordNavigatesheet(sheetState: SheetState, closeSheet: () -> Unit,
                             lat:MutableState<String>,lon:MutableState<String>,
                             temp:MutableState<String>, addressName:MutableState<String>, weather:MutableState<String>
    ){

        if(!momentPermission.checkPermission()){
            momentPermission.requestPermission()
            closeSheet()
        }else{
            momentLocation.getLocation().invoke().apply {
                this.addOnSuccessListener {
//                    x = "126.924776753718",
//                    y = "37.5456269289384"
                    lat.value = it.latitude.toString()
                    lon.value = it.longitude.toString()
                    kakaoViewModel.getLocal(
                        x = lon.value,
                        y = lat.value
                    )
                    openWeatherViewModel.getWeatherInCurrentLocation(
                        lat = lat.value,
                        lon = lon.value,
                        appid = "750af8c3ad235147ce30452e8242d76f"
                    )
                }
            }
        }


        val minute = remember {
            mutableStateOf(0)
        }
        val time = remember {
            mutableStateOf("00")
        }

        val customTimerDuration = remember {
            mutableStateOf(0)
        }


        val isPasused = remember {
            mutableStateOf(false)
        }


        val timerJob: Job = CoroutineScope(Dispatchers.Main).launch(start = CoroutineStart.LAZY) {
            withContext(Dispatchers.IO) {

                while (customTimerDuration.value >= 0) {

                    if(!isPasused.value){
                        delay(1000L)
                        customTimerDuration.value+=1
                        if(customTimerDuration.value==60){
                            customTimerDuration.value = 0
                            time.value = "0${customTimerDuration.value}"
                            minute.value+=1
                        }else if(customTimerDuration.value <10){
                            time.value = "0${customTimerDuration.value}"
                        }else{
                            time.value = "${customTimerDuration.value}"
                        }

                    }
                }
            }
        }







        var currentDate:String=""


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss"))

            currentDate = "${LocalDateTime.now()}"
        } else {
            val date = Date(System.currentTimeMillis())
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.KOREA
            )
            currentDate=dateFormat.format(date)
        }




        ModalBottomSheet(
            onDismissRequest = closeSheet,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
            containerColor = tertiary_500,
            dragHandle = null
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tertiary_500)
                    .height(248.dp),
            ) {
                Column(modifier = Modifier
                    .padding(top = 17.dp)
                    .align(Alignment.TopCenter)) {
                    Box(
                        modifier = Modifier.width(213.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Image(modifier = Modifier
                            .height(42.dp)
                            .width(213.dp),
                            painter =  painterResource(R.drawable.img_alarm_center_grey),
                            contentDescription = "record")
                        P_Medium11(content = "녹음은 한번에 최대 10분까지 가능해요\n" +
                                "최대 시간을 넘어가면 자동 종료 후 저장됩니다", color = white )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 63.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium18(content = "${minute.value}:${time.value}", color = black)
                    Column(modifier = Modifier.width(45.dp)) {
                        Divider(color = black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    P_Medium18(content = "열심히 듣고 있어요", color = black)
                    Column(modifier = Modifier.width(150.dp)) {
                        Divider(color = black)
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.clickable {
                            player.playFile(audioFile ?: return@clickable)
//                            coroutineScope
//                                .launch {
//                                    sheetState.hide()
//                                }
//                                .invokeOnCompletion {
//                                    if (!sheetState.isVisible) {
//                                        recordOpen.value = false
//                                    }
//                                }
                        }) {
                            YJ_Bold15("삭제", black)
                        }
                         Spacer(modifier = Modifier.width(46.dp))
                        Image(modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                if(!momentPermission.checkPermission()){
                                    momentPermission.requestPermission()
                                }else{
                                    if (timerJob.isActive) {
                                        isPasused.value = !isPasused.value

                                    } else {
                                        File(cacheDir, "audio.mp3").also {
                                            recorder.start(it)
                                            audioFile = it
                                        }
                                        timerJob.start()

                                    }
                                }



                            },
                            painter =  painterResource(R.drawable.ic_record_ing),
                            contentDescription = "record")
                        Spacer(modifier = Modifier.width(46.dp))

                        Column(
                            Modifier.clickable {
                                recorder.stop()
                                timerJob.cancel()

                                val requestFile = audioFile?.asRequestBody("audio/*".toMediaTypeOrNull())

                                val body = MultipartBody.Part.createFormData("recordFile",audioFile?.name,
                                    requestFile!!
                                )

                                val cardUploadDto=Gson().toJson(
                                    PostCardUploadReqeust(
                                        location = addressName.value,
                                        question = "",
                                        recordedAt = currentDate,
                                        temperature = temp.value,
                                        weather = weather.value
                                    )
                                )

                                val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
                                val gson = Gson()
                                val data = gson.toJson(cardUploadDto)
                                val requestbody = RequestBody.create(JSON, cardUploadDto)

                                cardViewModel.postCardUpload(
                                    cardUploadMultipart = requestbody,
                                    recordFile = body
                                )


                            }) {
                                YJ_Bold15("저장", primary_500)
                            }

                    }

                }
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Favorite(cardItems: MutableList<Card>, emotionList:MutableList<Emotion>){
        var expanded = remember { mutableStateOf(true) }

        val imageList = mutableStateListOf<File>()

//        cardItems.add(CardActivity.Card())
//        cardItems.add(CardActivity.Card())
//        cardItems.add(CardActivity.Card())


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color("#706969".toColorInt()),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    text = ApplicationClass.tripName,
                    textAlign = TextAlign.End,
                    fontSize = 22.sp,
                    fontFamily = FontMoment.preStandardFont,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color("#706969".toColorInt()),
                    thickness = 2.dp
                )
            }



            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                items(cardItems.size) { index ->
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = Color("#C3C8BC".toColorInt())
                                    ), shape = RoundedCornerShape(4.dp)
                                )
                                .animateContentSize()
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    cardItems[index].isExpand.value =
                                        !cardItems[index].isExpand.value
                                }

                        ) {
                            Spacer(modifier = Modifier.height(22.dp))
                            Column() {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 12.dp
                                    )
                                ) {
                                    Text(
                                        text = cardItems[index].cardView.recordFileName.split('T').first(),
                                        fontFamily = FontMoment.preStandardFont,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Text(
                                            text = "_00${index + 1}",
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Image(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .padding(top = 6.dp),
                                            painter = painterResource(id = R.drawable.ic_down),
                                            contentDescription = ""
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                        .height(2.dp)
                                        .background(color = Color("#706969".toColorInt()))
                                )
                            }


                            if (cardItems[index].isExpand.value) {
                                Spacer(modifier = Modifier.height(40.dp))
                                Column {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 22.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_location_grey),
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = cardItems[index].cardView.location,
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_weather_black),
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = cardItems[index].cardView.weather,
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 22.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_clock_grey),
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = cardItems[index].cardView.recordedAt.split("T")
                                                .first(),
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Divider(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(1.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = cardItems[index].cardView.recordedAt.split("T")
                                                .last(),
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(30.dp))

//                                LinearDeterminateIndicator()
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 23.dp)
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    listOf(
                                                        Color("#FBFAF7".toColorInt()),
                                                        Color("#C3C8BC".toColorInt())
                                                    )
                                                ),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    ) {
                                        FancyProgressBar(
                                            Modifier
                                                .height(12.dp)
                                                .fillMaxWidth(),
                                            onDragEnd = { finalProgress ->
                                                Log.d("aewgawegweag", finalProgress.toString())
                                                Log.e(
                                                    "finalProgress: ",
                                                    "${
                                                        String.format(
                                                            "%.0f",
                                                            (1 - finalProgress) * 100
                                                        )
                                                    }%"
                                                )


                                            }, onDrag = { progress ->
                                                Log.d("awegawegaew", progress.toString())
                                                Log.d(
                                                    "progress: ",
                                                    "${
                                                        String.format(
                                                            "%.0f",
                                                            (1 - progress) * 100
                                                        )
                                                    }%"
                                                )
                                            })
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                modifier = Modifier.clickable {
                                                    val file = File(MomentPath.RECORDER_PATH+ convertUrlLinkStringToRcorderNameString(cardItems[index].cardView.recordFileUrl))
                                                    if(file.exists()){
                                                        momentAudioPlayer.playFile(file)
                                                    }else{
                                                        Toast.makeText(this@MainActivity,"녹음파일이 없습니다.",
                                                            Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                painter = painterResource(id = R.drawable.ic_record_start),
                                                contentDescription = "seg"
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "질문리스트가 있다면 여기에 먼저 띄워질거에요",
                                                fontFamily = FontMoment.preStandardFont,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp,
                                                color = Color("#99342E".toColorInt())
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = cardItems[index].cardView.stt ?: "",
                                                fontFamily = FontMoment.preStandardFont,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_pencil),
                                                contentDescription = "edit"
                                            )
                                            Spacer(modifier = Modifier.height(18.dp))
                                        }

                                    }
                                    if (imageList.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    } else {


                                        Spacer(modifier = Modifier.height(8.dp))
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 23.dp),
                                            color = Color("#C3C1C1".toColorInt())
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 30.dp),
                                            horizontalArrangement = Arrangement.Start,
                                            content = {
                                                items(imageList.size) { index ->
                                                    Box(
                                                        modifier = Modifier
                                                            .size(72.dp)
                                                            .padding(end = 8.dp)
                                                    ) {
                                                        AsyncImage(
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .height(70.dp)
                                                                .width(66.dp)
                                                                .clip(RoundedCornerShape(6.dp)),
                                                            model = ImageRequest.Builder(this@MainActivity)
                                                                .data(imageList[index])
                                                                .build(),
                                                            contentDescription = "image"
                                                        )

                                                    }
                                                }
                                            })
                                    }

                                    Spacer(modifier = Modifier.height(30.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    ) {
                                        Text(
                                            text = "꽤나 즐거운 대화였네요",
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "꽤나 즐거운 대화였네요",
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 11.dp),
                                        color = Color("#706969".toColorInt())
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    ) {
                                        emotionList.forEach { item ->
                                            Row(
                                                modifier = Modifier.height(20.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    modifier = Modifier.size(12.dp),
                                                    painter = painterResource(id = item.icon),
                                                    contentDescription = ""
                                                )

                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = item.text,
                                                    fontFamily = FontMoment.preStandardFont,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 11.sp
                                                )
                                                Spacer(modifier = Modifier.width(26.dp))

                                                LinearProgressIndicator(
                                                    color = Color(item.color.toColorInt()),
                                                    progress = { item.persent }
                                                )
                                                Spacer(modifier = Modifier.width(36.dp))
                                                Text(
                                                    text = item.text,
                                                    fontFamily = FontMoment.preStandardFont,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            } else {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "꽤나 즐거운 대화였네요.",
                                        fontSize = 12.sp,
                                        color = Color("#938F8F".toColorInt()),
                                        fontFamily = FontMoment.preStandardFont,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(64.dp))
                                    Text(
                                        text = cardItems[index].cardView.weather,
                                        fontSize = 12.sp,
                                        color = Color("#938F8F".toColorInt()),
                                        fontFamily = FontMoment.preStandardFont,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Image(
                                        modifier = Modifier.size(18.dp),
                                        painter = painterResource(id = R.drawable.ic_weather_grey),
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }

                }
            }


        }
    }

    @Composable
    fun Setting(){

        val alarmbtnState = remember{ mutableStateOf(false) }
        val databtnState = remember{ mutableStateOf(false) }
        val InquirybtnState = remember{ mutableStateOf(false) }
        val versionbtnState = remember{ mutableStateOf(false) }


        Box(
            Modifier
                .background(tertiary_500)
                .fillMaxSize()
                .padding(horizontal = 30.dp)) {
            Column(
                Modifier
                    .padding(top = 40.dp)) {

                Column(modifier = Modifier
                    .width(83.dp)
                    .clickable { alarmbtnState.value = !alarmbtnState.value }){
                        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                            P_Medium18(
                                content = "알림 설정",
                                color = if(alarmbtnState.value) primary_500 else black
                            )
                        }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = if(alarmbtnState.value) primary_500 else black)
                }
                if (alarmbtnState.value){
                    Spacer(modifier = Modifier.height(8.dp))
                    Toggle()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                else{
                    Spacer(modifier = Modifier.height(40.dp))
                }



                Column(modifier = Modifier
                    .width(99.dp)
                    .clickable { databtnState.value = !databtnState.value }){
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        P_Medium18(
                            content = "데이터 허용",
                            color = if(databtnState.value) primary_500 else black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = if(databtnState.value) primary_500 else black)
                }
                if (databtnState.value){
                    Spacer(modifier = Modifier.height(8.dp))
                    Toggle()
                    Spacer(modifier = Modifier.height(8.dp))
                    P_Medium11(content = "셀룰러 데이터를 허용하면, 데이터 환경에서도 녹음카드 분석 가능해요\n" +
                            "허용하지 않으면, Wi-Fi가 연결된 환경에서만 분석돼요", color = neutral_600)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                else{
                    Spacer(modifier = Modifier.height(40.dp))
                }


                Column(modifier = Modifier
                    .width(79.dp)
                    .clickable { InquirybtnState.value = !InquirybtnState.value }){
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        P_Medium18(
                            content = "문의하기",
                            color = if(InquirybtnState.value) primary_500 else black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = if(InquirybtnState.value) primary_500 else black)
                }
                if (InquirybtnState.value){
                    Spacer(modifier = Modifier.height(8.dp))
                    P_Medium14(content = "kookminmoment@gmail.com", color = black)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                else{
                    Spacer(modifier = Modifier.height(40.dp))
                }


                Column(modifier = Modifier
                    .width(79.dp)
                    .clickable { versionbtnState.value = !versionbtnState.value }){
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        P_Medium18(
                            content = "버전안내",
                            color =  if (versionbtnState.value) primary_500 else black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = if (versionbtnState.value) primary_500 else black)
                }
                if (versionbtnState.value){
                    Spacer(modifier = Modifier.height(8.dp))
                    P_Medium14(content = "v1.1", color = black)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                else{
                    Spacer(modifier = Modifier.height(40.dp))
                }

                Column(
                    Modifier
                        .padding(top = 150.dp)) {
                    Column(Modifier.clickable {
                        tokenSharedPreferences.edit().putString("accessToken","").apply()
                        startActivity(Intent(this@MainActivity,SplashActivity::class.java))
                        finish()
                    }) {
                        P_Medium14("로그아웃", black)
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Column(Modifier.clickable {
                        tokenSharedPreferences.edit().putString("accessToken","").apply()
                        startActivity(Intent(this@MainActivity,SplashActivity::class.java))
                        finish()
                    }) {
                        P_Medium14("탈퇴하기", black)
                    }
                }
        }
    }
    }

    @Composable
    fun Toggle() {

        val states = listOf(
            "켜기",
            "끄기"
        )
        var selectedOption by remember { mutableStateOf(states[0])}
        val onSelectionChange = { text: String ->
            selectedOption = text
        }

        Row(modifier = Modifier
            .width(120.dp)
            .height(40.dp)
            .clip(shape = RoundedCornerShape(3.dp))
            .background(secondary_50)
            ) {
                states.forEach { text ->
                    Column(  modifier = Modifier
                        .width(60.dp)
                        .height(40.dp)
                        .clip(shape = RoundedCornerShape(3.dp))
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                primary_500
                            } else {
                                secondary_50
                            }
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        P_Medium14(content = text,
                            color = if (text == selectedOption) white else neutral_500 )
                    }

                }
            }
    }

    @Composable
    fun MiniTheme1(receiptAll:ReceiptAll){
        val emotionList = emotionPercent(receiptAll.neutral,receiptAll.happy,receiptAll.angry,receiptAll.sad, receiptAll.disgust)

        Box(
            modifier = Modifier
                .height(244.dp)
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .background(
                    color = white,
                    shape = RoundedCornerShape(2.dp)
                )
                .border(
                    BorderStroke((0.2).dp, black),
                    shape = RoundedCornerShape(2.dp)
                )
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .height(17.7.dp)
                        .fillMaxWidth()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                        )
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    P_Medium(receiptAll.tripName, white , 5.sp)

                    Image(
                        modifier = Modifier
                            .height(17.dp)
                            .width(30.dp)
                            .padding(end = 3.dp),
                        painter = painterResource(R.drawable.img_logo_white),
                        contentDescription = "로고 화이트"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium(receiptAll.oneLineMemo,neutral_500, 5.sp)

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if(receiptAll.subDeparture !=""){
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(5.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    P_Medium(receiptAll.subDeparture,primary_500, 4.5.sp)
                }else{}
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 55.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_ExtraBold(receiptAll.mainDeparture, primary_500, 18.sp)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 87.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_airplain_red),
                    contentDescription = "장소",
                    Modifier.size(19.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 116.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if(receiptAll.subDestination != ""){
                    Image(
                        painter = painterResource(R.drawable.ic_location_red),
                        contentDescription = "장소",
                        Modifier.size(5.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    P_Medium(receiptAll.subDestination,primary_500, 4.5.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 123.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_ExtraBold(receiptAll.mainDestination, primary_500, 18.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 150.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_red),
                    contentDescription = "가위",
                    modifier = Modifier
                        .height(7.dp)
                        .fillMaxWidth()
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 160.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .wrapContentWidth()
                        .height(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    P_Medium(content = "여행 카드", color = neutral_500, 4.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    YJ_Bold(content = receiptAll.numOfCard.toString(), color = primary_500, 5.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    P_Medium(content = "여행 날짜", color = neutral_500, 4.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    P_Medium(content = receiptAll.stDate, color = primary_500, 4.sp)
                    P_Medium(content = receiptAll.edDate, color = primary_500, 4.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    P_Medium(content = "여행 감정", color = neutral_500, 4.sp)
                    Spacer(modifier = Modifier.height(3.8.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                    ) {
                        emotionList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .height(6.dp)
                                    .wrapContentWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.width(40.dp)
                                )  {
                                    LinearProgressIndicator(
                                        progress = { item.persent.toFloat() / 100 },
                                        modifier = Modifier.height(3.dp),
                                        color = when (index) {
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
                                Spacer(modifier = Modifier.width(4.2.dp))
                                Image(
                                    modifier = Modifier.size(5.dp),
                                    painter = painterResource(
                                        id = Theme1_Emotion(
                                            item.text,
                                            index
                                        )
                                    ),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                P_Medium(
                                    content = item.persent.toString() + "%",
                                    color = when (index) {
                                        0 -> primary_500
                                        1 -> neutral_600
                                        2 -> neutral_400
                                        3 -> neutral_200
                                        4 -> neutral_100
                                        else -> primary_500
                                    }, 4.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MiniTheme2(receiptAll:ReceiptAll){
        val emotionList = emotionPercent(receiptAll.neutral,receiptAll.happy,receiptAll.angry,receiptAll.sad, receiptAll.disgust)

        Box(
            modifier = Modifier
                .height(244.dp)
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .background(
                    color = white,
                    shape = RoundedCornerShape(2.dp)
                )
                .border(
                    BorderStroke((0.2).dp, neutral_500),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        {
            Column(
                modifier = Modifier
                    .width(3.74.dp)
                    .padding(top = 15.dp)
                    .fillMaxHeight()
                    .background(
                        color = primary_500,
                        shape = RoundedCornerShape(bottomEnd = 2.dp)
                    )
                    .align(Alignment.BottomEnd)
            ) {}

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, start = 10.dp)
            ) {
                Column( modifier = Modifier
                    .padding(start = 4.dp)) {
                    P_Medium(content = receiptAll.oneLineMemo, color = neutral_500, 5.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Divider(color = primary_500, thickness = 1.dp)
                Spacer(modifier = Modifier.height(15.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_location_grey),
                            contentDescription = "장소",
                            Modifier.size(5.dp)
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        P_Medium(content = receiptAll.subDeparture, color =neutral_600, 4.5.sp)
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        P_ExtraBold(content = receiptAll.mainDeparture, color = black, size = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_train_grey),
                            contentDescription = "기차",
                            modifier = Modifier
                                .height(34.dp)
                                .width(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_location_grey),
                            contentDescription = "장소",
                            Modifier.size(5.dp)
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        P_Medium(content = receiptAll.subDestination, color = neutral_600, 4.5.sp)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        P_ExtraBold(content = receiptAll.mainDestination, color = black, size = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(27.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        P_Medium(content = receiptAll.tripName, color = black, 5.sp)
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                Modifier.width(40.dp)
                            ) {
                                P_Medium(content = "여행 감정", color = neutral_500, 4.sp)
                                Spacer(modifier = Modifier.height(3.8.dp))
                                Column(
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    emotionList.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier.height(5.5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Image(
                                                modifier = Modifier.size(5.dp),
                                                painter = painterResource(
                                                    id = Theme2_Emotion(
                                                        kind = item.text
                                                    )
                                                ),
                                                contentDescription = ""
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Column(
                                                modifier = Modifier.width(32.dp)
                                            ) {
                                                LinearProgressIndicator(
                                                    progress = { item.persent.toFloat() / 100 },
                                                    modifier = Modifier.height(1.5.dp),
                                                    color = when (index) {
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
                                Modifier.padding(start = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                P_Medium(content = "카드 갯수", color = neutral_500, 4.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Column(
                                    Modifier
                                        .size(23.dp)
                                        .background(
                                            color = white,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            width = 0.3.dp,
                                            color = black,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    YJ_Bold(content = receiptAll.numOfCard.toString(), color = primary_500, 8.sp)
                                }
                            }
                        }
                    }
                }
            }

            Row(
                Modifier
                    .padding(bottom = 2.dp, end = 7.dp)
                    .align(Alignment.BottomEnd)
            ) {
                P_Medium(content = receiptAll.stDate, color = neutral_500, 4.sp)
                P_Medium(content = " / ", color = neutral_500, 4.sp)
                P_Medium(content = receiptAll.edDate, color = neutral_500, 4.sp)
            }

            Row() {
                Column(
                    modifier = Modifier
                        .width(10.dp)
                        .fillMaxHeight()
                        .background(
                            color = primary_500,
                            shape = RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_logo_vertical),
                        contentDescription = "로고",
                        Modifier
                            .width(30.dp)
                            .fillMaxHeight()
                    )
                }
            }
            Column(Modifier.padding(top = 140.dp)) {
                Image(
                    painter = painterResource(R.drawable.img_cutline_circle),
                    contentDescription = "로고",
                    Modifier
                        .height(4.5.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun deleteDialog(showDeleteDialog : MutableState<Boolean>, DeleteReceipt : MutableList<ReceiptId>){

        CustomTitleCheckDialog(
            title = "${DeleteReceipt.size} 개의 영수증을 정말 삭제 할까요?",
            description = "삭제된 영수증은 복구할 수 없어요",
            checkleft = "네",
            checkright = "아니요",
            onClickCancel = { showDeleteDialog.value = false },
            onClickleft = {
                receiptViewModel.deleteReceiptDelete(
                    body = deleteReceiptDeleteRequest(receiptIds = DeleteReceipt))
                showDeleteDialog.value = false },
            onClickright = { showDeleteDialog.value = false }
        )
    }

    fun emotionPercent(common : Double, happy : Double, angry : Double, sad : Double, disgust : Double): SnapshotStateList<com.capstone.android.application.data.local.Emotion> {
        val emotionList = mutableStateListOf<com.capstone.android.application.data.local.Emotion>()
        emotionList.add(
            com.capstone.android.application.data.local.Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = (common).toInt()
            )
        )
        emotionList.add(
            com.capstone.android.application.data.local.Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = (happy).toInt()
            )
        )
        emotionList.add(
            com.capstone.android.application.data.local.Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = (angry).toInt()
            )
        )
        emotionList.add(
            com.capstone.android.application.data.local.Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요",
                persent = (sad).toInt()
            )
        )
        emotionList.add(
            com.capstone.android.application.data.local.Emotion(
                icon = R.drawable.ic_receipt2_emotion_disgust,
                text = "불쾌해요",
                persent = (disgust).toInt()
                )
        )

        emotionList.sortByDescending { it.persent }
        return emotionList
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

    @SuppressLint("UnrememberedMutableState")
    @Preview(apiLevel = 33)
    @Composable
    fun MainPreview() {
        ApplicationTheme {
//            ReceiptCardChoice()
//            Home()
//            ItemTrip()
//            RecordDaily()
//            Setting()
//            ItemTrip()
            //Receipt()
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
}



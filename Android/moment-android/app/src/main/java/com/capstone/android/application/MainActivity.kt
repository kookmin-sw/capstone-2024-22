package com.capstone.android.application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.capstone.android.application.app.composable.FancyProgressBar
import com.capstone.android.application.app.screen.MainScreen
import com.capstone.android.application.app.screen.BottomNavItem
import com.capstone.android.application.domain.Trip
import com.capstone.android.application.presentation.TripViewModel
import com.capstone.android.application.ui.CardActivity
import com.capstone.android.application.ui.PostTripActivity
import com.capstone.android.application.ui.ReciptActivity
import com.capstone.android.application.ui.TripFileActivity
import com.capstone.android.application.ui.theme.ApplicationTheme
import com.capstone.android.application.ui.theme.BigButton
import com.capstone.android.application.ui.theme.FontMoment
import com.capstone.android.application.ui.theme.P_Medium11
import com.capstone.android.application.ui.theme.P_Medium14
import com.capstone.android.application.ui.theme.P_Medium18
import com.capstone.android.application.ui.theme.YJ_Bold15
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_500
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.secondary_50
import com.capstone.android.application.ui.theme.tertiary_500
import com.capstone.android.application.ui.theme.white
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val tripViewModel : TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ApplicationTheme {
                // A surface container using the 'background' color from the theme
                MainRoot()
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainRoot(){
        val tripList = remember {
            mutableStateListOf<Trip>()
        }
        tripViewModel.getTripAll()
        tripViewModel.getTripAllSuccess.observe(this@MainActivity){ response->
            response.data.trips.forEach {

                tripList.add(Trip(
                    id=it.id,tripName = it.tripName, startDate = it.startDate, endDate = it.endDate
                ))
            }

        }
        tripViewModel.getTripAllFailure.observe(this@MainActivity){ response->
            Log.d("weagewagaew","${response.exception.message}")
        }


        navController = rememberNavController()
        val bottomItems = listOf<BottomNavItem>(
            BottomNavItem.Home,
            BottomNavItem.Receipt,
            BottomNavItem.Record,
            BottomNavItem.Favorite,
            BottomNavItem.Setting,
        )

        val scope = rememberCoroutineScope()
        val title = remember{
            mutableStateOf("추가")
        }

        val currentSelectedBottomRoute = remember{
            mutableStateOf("홈")
        }
        //bottomsheet
        val sheetState = rememberModalBottomSheetState(/*
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {false}*/
        )
        var recordOpen = remember { mutableStateOf(false)}

        if(recordOpen.value) {
            RecordNavigatesheet(recordOpen,
                sheetState = sheetState,
                closeSheet = { recordOpen.value = false })
        }

        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp / 2)
                .shadow(elevation = 6.dp)
            ,
            bottomBar = {
                Box(
                    contentAlignment = Alignment.Center
                ){
                    BottomNavigation(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                        ,
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
                                        ){
                                            Divider(
                                                modifier = Modifier.fillMaxWidth(),
                                                thickness = (0.8).dp, color = Color.Black
                                            )
                                            if(screen?.screenRoute==currentSelectedBottomRoute.value){
                                                Divider(
                                                    modifier = Modifier
                                                        .width(36.dp)
                                                        .padding(top = (1.5).dp),
                                                    thickness = 4.dp, color = Color("#99342E".toColorInt())
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        if(screen.selectedDrawableId!=0) {
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
                                    if(screen!=BottomNavItem.Record){
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
                            }
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(id = R.drawable.ic_record), contentDescription = ""
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
                                .padding(end = 20.dp)
                            ,
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            navBackStackEntry.let {
                                if(it==null){
                                    Log.d("waegfwa","null")
                                }else{
                                    Log.d("waegfwa",it.destination.route.toString())

                                }
                            }


                            navController.currentDestination.let {
                                if(it==null || it.route=="Home"){

                                }else{
                                    Log.d("wegewag",it.route.toString())

                                    Text(
                                        text = "뒤로",
                                        fontFamily = FontMoment.obangFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Spacer(Modifier.weight(1f))
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        when(title.value){
                                            "추가" -> {startActivity(Intent(this@MainActivity,PostTripActivity::class.java))}
                                            "영수증 모아보기" -> {}
                                            "작게보기" -> {}
                                        }
                                    },
                                text = title.value,
                                fontFamily = FontMoment.obangFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                    }

                )
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = BottomNavItem.Home.screenRoute,
                Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Home.screenRoute) {
                    currentSelectedBottomRoute.value = "Home"
                    Log.d("waegwagewa",tripList.toString())
                    Home(tripList)
                    title.value = "추가"
                }
                composable(BottomNavItem.Receipt.screenRoute) {
                    currentSelectedBottomRoute.value = "Receipt"

                    Receipt()
                    title.value = "영수증 모아보기"
                }
                composable(BottomNavItem.Record.screenRoute) {
                    currentSelectedBottomRoute.value = "Record"

                    Record()

                }
                composable(BottomNavItem.Favorite.screenRoute) {
                    currentSelectedBottomRoute.value = "Favorite"

                    Favorite()
                    title.value = "작게보기"
                }
                composable(BottomNavItem.Setting.screenRoute) {
                    currentSelectedBottomRoute.value = "Setting"
                    Setting()
                    title.value = ""
                }
                composable(MainScreen.ReceiptPost.screenRoute){
                    currentSelectedBottomRoute.value = MainScreen.ReceiptPost.rootRoute
                    ReceiptPost()
                }

                composable(MainScreen.HomeTrip.screenRoute){
                    currentSelectedBottomRoute.value = MainScreen.HomeTrip.rootRoute
                    HomeTrip()
                }
                composable(MainScreen.RecordDaily.screenRoute){
                    currentSelectedBottomRoute.value = MainScreen.RecordDaily.rootRoute

                    RecordDaily()
                }
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
    fun Home(tripList:MutableList<Trip>){


        Log.d("weagawegawe",tripList.toString())

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
                        Column {
                            Text(
                                text = "전라도의 선유도",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Row {
                                Text(
                                    text = "출발까지",
                                    color = Color("#706969".toColorInt()),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "6",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 45.sp,
                                    color = Color("#99342E".toColorInt())
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    modifier = Modifier.align(Alignment.Bottom),
                                    text = "일 남았아요",
                                    color = Color("#706969".toColorInt()),
                                    fontSize = 16.sp
                                )

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

            LazyColumn(
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
            ){
                items(
                    count = tripList.size,
                    itemContent = {index->
                        ItemTrip(type = 0, tripName = tripList[index].tripName,startDate=tripList[index].startDate, endDate = tripList[index].endDate)

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

    @Composable
    fun RecordDaily(){
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
                    count = 8,
                    itemContent = {
                        Column(
                            modifier = Modifier.clickable {
                                startActivity(Intent(this@MainActivity,TripFileActivity::class.java))
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
                                        text = "2024.03.05",
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
                                        text = "4개의 파일이 있어요",
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
    fun ItemTrip(type:Int,tripName:String,startDate:String,endDate:String){
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
                startActivity(Intent(this@MainActivity,TripFileActivity::class.java))
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
                            Toast.makeText(this@MainActivity,"수정",Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(this@MainActivity,"삭제",Toast.LENGTH_SHORT).show()
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
    fun Receipt(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.test_image), contentDescription = "test"
            )
            Spacer(modifier = Modifier.height(8.dp))
            BigButton("만들기", true,  onClick = {startActivity(Intent(this@MainActivity, ReciptActivity::class.java))})

        }
    }

    @Composable
    fun ReceiptPost(){
        //영수증 게시
        Text(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate(MainScreen.ReceiptPost.screenRoute)
                },
            text = "만들기",
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun Record(){


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun  RecordNavigatesheet(recordOpen : MutableState<Boolean>, sheetState: SheetState, closeSheet: () -> Unit
    ){

        val coroutineScope = rememberCoroutineScope()

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
                    P_Medium18(content = "0:03", color = black)
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
                            coroutineScope
                                .launch {
                                    sheetState.hide()
                                }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        recordOpen.value = false
                                    }
                                }
                        }) {
                            YJ_Bold15("삭제", black)
                        }
                         Spacer(modifier = Modifier.width(46.dp))
                        Image(modifier = Modifier.size(50.dp),
                            painter =  painterResource(R.drawable.ic_record_ing),
                            contentDescription = "record")
                        Spacer(modifier = Modifier.width(46.dp))

                        Column(Modifier.clickable {
                            coroutineScope
                                .launch {
                                    sheetState.hide()
                                }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        recordOpen.value = false
                                    }
                                }
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
    fun Favorite(){
        var expanded = remember { mutableStateOf(true) }

        val imageList = mutableStateListOf<File>()
        val cardItems = mutableStateListOf<CardActivity.Card>()
        val emotionList = mutableStateListOf<CardActivity.Emotion>()

        cardItems.add(CardActivity.Card())
        cardItems.add(CardActivity.Card())
        cardItems.add(CardActivity.Card())

        emotionList.add(
            CardActivity.Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = "60%"
            )
        )
        emotionList.add(
            CardActivity.Emotion(
                icon = R.drawable.ic_emotion_happy,
                text = "즐거워요",
                persent = "20%"
            )
        )
        emotionList.add(
            CardActivity.Emotion(
                icon = R.drawable.ic_emotion_angry,
                text = "화가나요",
                persent = "15%"
            )
        )
        emotionList.add(
            CardActivity.Emotion(
                icon = R.drawable.ic_emotion_sad,
                text = "슬퍼요 ",
                persent = "5%"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                }
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
                    text = "전라도의 선유도",
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
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clickable {
                                                cardItems[index].isFavorite.value =
                                                    !cardItems[index].isFavorite.value
                                            }
                                        ,
                                        painter = painterResource(id = if(cardItems[index].isFavorite.value) R.drawable.ic_heart_red else R.drawable.ic_heart_white),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = "ss",
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
                                            text = "wegwae",
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
                                            text = "wegwae",
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
                                            text = "wegwae",
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
                                            text = "wegwae",
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(30.dp))

//                                LinearDeterminateIndicator()
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp)
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
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
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
                                                text = "분석된 글이 쓰여지는 공간입니다. 칸의 가로 넓이는 고정되어있습니다. 칸의 사이즈별 마진은 동일합니다. 즉 모바일 사이즈 마진에 맞춰 칸이 늘어나거나 줄어들며 글자 쓰는 공간도 칸에 따라 유동적으로 변경됩니다. 칸 안의 글자 양 옆 마진은 8이며 칸의 모바일 사이즈 마진은 큰 카드가 왼 20px, 오 20px이며 / 글이 쓰여지는 공간의 마진은 카드마진 기준으로 왼 24px, 오23px입니다. 좌우가 다른이유는 아이폰 미니의 화면비율이 홀수라서 그렇습니다. 은근히 신경쓰이네요 또, 가로글자수 제한은 있지만 세로로는 제한이없습니다. 카드의 길이가 무한정 길어질 수도 있다는 말이지요. 세로에도 제한을 두는 것이 좋을까요?",
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
                                    if (imageList.isNotEmpty()) {
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
                                                    progress = { 0.4f }
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
                                        text = "해가 쨍쨍한 날",
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
                    .width(134.dp)
                    .clickable { }){
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        P_Medium18(
                            content = "계정 이메일 변경",
                            color = black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = black)
                }
                Spacer(modifier = Modifier.height(40.dp))


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
                    Column(Modifier.clickable {  }) {
                        P_Medium14("로그아웃", black)
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Column(Modifier.clickable {  }) {
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



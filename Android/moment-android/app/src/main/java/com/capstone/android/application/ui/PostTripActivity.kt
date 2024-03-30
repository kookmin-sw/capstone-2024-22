package com.capstone.android.application.ui

import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.TextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.capstone.android.application.ui.theme.FontMoment

class PostTripActivity:ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color("#FBFAF7".toColorInt())),
                topBar = {
                    TopAppBar(
                        actions = {

                        },
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                ,
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.width(20.dp))

                                Text(
                                    modifier = Modifier
                                        .clickable {
                                        },
                                    text = "취소",
                                    fontFamily = FontMoment.obangFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                        },
                                    text = "완료",
                                    fontFamily = FontMoment.obangFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color("#938F8F".toColorInt())
                                )
                                Spacer(modifier = Modifier.width(40.dp))
                            }

                        }

                    )
                }
            ){ innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 26.dp)
                ) {
                    Main()
                }

            }
        }
    }

    @Composable
    fun Main(){
        val text = remember{
            mutableStateOf("")
        }

        val currentCalendarFocus = remember{
            mutableStateOf(-1)
        }

        val startDate = remember{
            mutableStateOf("출발 날짜")
        }

        val endDate = remember{
            mutableStateOf("도착 날짜")
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color("#706969".toColorInt()),
                thickness = 2.dp
            )
            Spacer(modifier = Modifier.height(60.dp))
            TextField(
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth(),
                value = text.value, onValueChange = {text.value = it},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black
                ),
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "여행 파일의 이름은 13글자까지 가능해요",
                        color = Color("#E7E6E6".toColorInt()),
                        fontSize = 16.sp
                    )
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .background(color = if (currentCalendarFocus.value == 0) Color.White else Color.Transparent)
                        .clickable {
                            currentCalendarFocus.value = 0
                        }
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = startDate.value,
                        fontSize = 16.sp,
                        color = if(currentCalendarFocus.value==0) Color.Black else Color("#E7E6E6".toColorInt())
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(
                        thickness = 1.dp,
                        color = if(currentCalendarFocus.value==0) Color.White else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .background(color = if (currentCalendarFocus.value == 0) Color.White else Color.Transparent)
                        .clickable {
                            currentCalendarFocus.value = 1
                        }
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = endDate.value,
                        fontSize = 16.sp,
                        color = if(currentCalendarFocus.value==1) Color.Black else Color("#E7E6E6".toColorInt())
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(
                        thickness = 1.dp,
                        color = if(currentCalendarFocus.value==1) Color.White else Color.Black
                    )
                }
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                factory = { CalendarView(it) },
                update = {
                    it.setOnDateChangeListener{calendarView,year,month,day->
                        if(currentCalendarFocus.value == 0){
                            startDate.value = "$year.$month.$day"
                            it.minDate
                        }else{
                            endDate.value = "$year.$month.$day"
                        }
                    }
                }
            )

        }
    }

    @Preview
    @Composable
    fun PostTripActivityPreview(){
        Main()
    }
}
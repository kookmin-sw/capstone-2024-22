package com.capstone.android.application.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.capstone.android.application.ui.theme.FontMoment

class TripFileActivity:ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                ,
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
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                             finish()
                                        },
                                    text = "뒤로",
                                    fontFamily = FontMoment.obangFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                        }

                    )
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    Main()
                }

            }
        }
    }

    @Composable
    fun Main(){
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
                ,
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


            LazyColumn(
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
            ){
                items(
                    count = 8,
                    itemContent = {index->
                        Column(
                            modifier = Modifier.clickable {
                                startActivity(Intent(this@TripFileActivity,CardActivity::class.java))
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
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${index+1}일차",
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "2024.03.05",
                                            fontSize = 11.sp,
                                            fontFamily = FontMoment.preStandardFont,
                                            fontWeight = FontWeight.Medium,
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
    
    @Preview
    @Composable
    fun TripFileActivityPreview(){
        Main()
    }
}
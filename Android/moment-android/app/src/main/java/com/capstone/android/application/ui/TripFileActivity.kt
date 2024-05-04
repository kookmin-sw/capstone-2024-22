package com.capstone.android.application.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.domain.TripFile
import com.capstone.android.application.presentation.TripFileViewModel
import com.capstone.android.application.ui.theme.FontMoment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TripFileActivity:ComponentActivity() {
    private val tripFileViewModel:TripFileViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent = intent




        setContent {
            val tripId = remember {
                mutableStateOf(0)
            }
            val tripFileList = remember {
                mutableStateListOf<TripFile>()
            }
            try {
                mainIntent?.let {
                    tripId.value = it.getIntExtra("tripId",0)
                }
            }catch (e: Exception){
                Toast.makeText(this@TripFileActivity,"server error", Toast.LENGTH_SHORT).show()
                finish()
            }

            tripFileViewModel.getTripFileAll(
                tripId = tripId.value
            )
            tripFileViewModel.getTripFileSuccess.observe(this@TripFileActivity){ response ->
                Timber.i(response.toString())
                response.data.tripFiles.mapNotNull { tripFile-> runCatching {
                    TripFile(
                        id = tripFile.id , tripId = tripFile.tripId,
                        yearDate = tripFile.yearDate, analyzingCount = mutableStateOf(tripFile.totalCount)
                    )
                    }.onSuccess {
                        tripFileList.clear()
                    }
                    .onFailure {

                    }
                    .getOrNull()
                }.forEach {
                    tripFileList.add(it)
                }
            }

            tripFileViewModel.getTripFileFailure.observe(this@TripFileActivity){ error ->


            }


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
                    Main(tripFileList = tripFileList)
                }

            }
        }
    }

    @Composable
    fun Main(tripFileList:MutableList<TripFile>){
        val cardActivityContract =
            rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d("waegwegewa","${result.resultCode}")
                if (result.resultCode == 1) {

                    try {
                        result.data?.let {
                            val index = it.getIntExtra("tripFileListIndex",0)
                            val totalCount = it.getIntExtra("totalCount",0)
                            tripFileList[index].analyzingCount.value=totalCount

                        }
                    }catch(e:Exception){
                        Toast.makeText(this@TripFileActivity,"server error",Toast.LENGTH_SHORT).show()
                    }

                    //do something here
                }

            }
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


            LazyColumn(
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
            ){
                items(
                    count = tripFileList.size,
                    itemContent = {index->
                        Column(
                            modifier = Modifier.clickable {
                                val intent = Intent(this@TripFileActivity,CardActivity::class.java)
                                intent.putExtra("tripFileId",tripFileList[index].id)
                                intent.putExtra("tripFileListIndex",index)
                                cardActivityContract.launch(intent)
                            } ,
                        ) {
                            Box(
                                modifier = Modifier.clip(RectangleShape),
                                contentAlignment = Alignment.CenterEnd
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .fillMaxSize(),
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
                                            text = tripFileList[index].yearDate,
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
                                        text = "${tripFileList[index].analyzingCount.value}개의 파일이 있어요",
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
//        Main()
    }
}
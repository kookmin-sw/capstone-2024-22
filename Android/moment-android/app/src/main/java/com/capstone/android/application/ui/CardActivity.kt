package com.capstone.android.application.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import android.Manifest
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.capstone.android.application.R
import com.capstone.android.application.app.composable.FancyProgressBar
import com.capstone.android.application.app.utile.recorder.AndroidAudioPlayer
import com.capstone.android.application.app.utile.recorder.MomentAudioRecorder
import com.capstone.android.application.domain.Card
import com.capstone.android.application.presentation.CardViewModel
import com.capstone.android.application.ui.theme.FontMoment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.io.File
import java.lang.Exception
import androidx.compose.ui.Alignment.Companion as Alignment1

@AndroidEntryPoint
class CardActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    private val cardViewModel:CardViewModel by viewModels()

    private val recorder by lazy {
        MomentAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this@CardActivity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        val mainIntent = intent





        setContent {
            val cardItems = remember {
                mutableStateListOf<Card>()
            }

            val tripFileId = remember {
                mutableStateOf(0)
            }
            try {
                mainIntent?.let {
                    tripFileId.value = it.getIntExtra("tripFileId",0)
                }
            }catch (e: Exception){
                Toast.makeText(this@CardActivity,"server error", Toast.LENGTH_SHORT).show()
                finish()
            }

            var isEdit = remember { mutableStateOf(false) }

            cardViewModel.getCardAll(
                tripFileId = tripFileId.value
            )
            cardViewModel.getCardAllSuccess.observe(this@CardActivity){ response ->
                cardItems.clear()
                response.data.cardViews.mapNotNull { card-> runCatching {
                    Card(
                        cardView = card
                    )

                }.onSuccess {}.onFailure {}.getOrNull()
                }.forEach {
                    cardItems.add(it)
                }
            }

            cardViewModel.getCardAllFailure.observe(this@CardActivity){ error ->

            }


            Scaffold(
                modifier = Modifier
                    .fillMaxWidth(),
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
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment1.CenterVertically,
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

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            isEdit.value = !isEdit.value
                                        },
                                    text = "삭제",
                                    fontFamily = FontMoment.obangFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(14.dp))
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
                    Main(isEdit = isEdit,cardItems = cardItems)
                }

            }
        }

    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    data class Emotion(
        val icon: Int,
        val text: String,
        val persent: String
    )




    @Composable
    fun Main(isEdit:MutableState<Boolean> , cardItems: MutableList<Card>) {
        var expanded = remember { mutableStateOf(true) }

        val imageList = mutableStateListOf<File>()
        val emotionList = mutableStateListOf<Emotion>()

        emotionList.add(
            Emotion(
                icon = R.drawable.ic_emotion_common,
                text = "평범해요",
                persent = "60%"
            )
        )
        emotionList.add(Emotion(icon = R.drawable.ic_emotion_happy, text = "즐거워요", persent = "20%"))
        emotionList.add(Emotion(icon = R.drawable.ic_emotion_angry, text = "화가나요", persent = "15%"))
        emotionList.add(Emotion(icon = R.drawable.ic_emotion_sad, text = "슬퍼요 ", persent = "5%"))

        val pickMultipleMedia = rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(10)
        ) { uris ->

            if (uris.isNotEmpty()) {
                uris.forEach {
                    Log.d("awegwaeg", it.path.toString())

                    val file = File(getPath(it))
                    imageList.add(file)
                }


//                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//
//                val body = MultipartBody.Part.createFormData("mainImage", file.name, requestFile)


            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isEdit.value = !isEdit.value
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
                    .padding(horizontal = if (isEdit.value) 0.dp else 20.dp)
            ) {
                items(cardItems.size) { index ->
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEdit.value) {
                            Spacer(modifier = Modifier.width(40.dp))
                            Image(
                                modifier = Modifier
                                    .clickable {

                                        cardItems[index].isDelete.value = !cardItems[index].isDelete.value
                                        if(!cardItems[index].isDelete.value){
                                            cardItems.forEach{
                                                it.isDelete.value = false
                                            }
                                        }
                                    },
                                painter = painterResource(id = if(cardItems[index].isDelete.value) R.drawable.ic_box_check else R.drawable.ic_box_uncheck),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment1.CenterHorizontally,
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


                            if (cardItems[index].isExpand.value && !isEdit.value) {
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
                                        verticalAlignment = Alignment1.CenterVertically
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
                                    if (imageList.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 24.dp)
                                                .clickable {
                                                    pickMultipleMedia.launch(
                                                        PickVisualMediaRequest(
                                                            ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                                        )
                                                    )
                                                },
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "사진추가",
                                                fontFamily = FontMoment.preStandardFont,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 12.sp
                                            )
                                            Image(
                                                painter = painterResource(R.drawable.ic_plus_image),
                                                contentDescription = "image plus"
                                            )
                                        }
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
                                                            model = ImageRequest.Builder(this@CardActivity)
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
    fun test() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            var expanded = remember { mutableStateOf(false) }

            LazyColumn {
                items(3) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .background(color = Color.Blue)
                            .animateContentSize()
                            .height(if (expanded.value) 100.dp else 50.dp)
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                expanded.value = !expanded.value
                            }

                    ) {
                        Text(
                            text = "weailghaewklghjewa"
                        )
                        Text(
                            modifier = Modifier
                                .background(color = Color.Blue)
                                .animateContentSize()
                                .height(if (expanded.value) 100.dp else 50.dp),
                            text = "ewagweg",
                            fontSize = if (expanded.value) 32.sp else 16.sp
                        )
                        if (expanded.value) {

                        } else {

                        }


                    }
                }
            }

        }
    }

    @Composable
    fun recordTest() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment1.CenterHorizontally
        ) {
            Button(onClick = {
                File(cacheDir, "audio.mp3").also {
                    recorder.start(it)
                    audioFile = it
                }
            }) {
                Text(text = "Start recording")
            }
            Button(onClick = {
                recorder.stop()
                Log.d("waegwagew", "${audioFile?.path}")

            }) {
                Text(text = "Stop recording")

            }
            Button(onClick = {
                player.playFile(audioFile ?: return@Button)
            }) {
                Text(text = "Play")
            }
            Button(onClick = {
                player.stop()
            }) {
                Text(text = "Stop playing")
            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                recorder.pause()
            }) {
                Text(text = "일시정지")
            }

            Button(onClick = {
                recorder.restart()
            }) {
                Text(text = "재시작")
            }


//                NavHost(
//                    navController,
//                    startDestination = "main",
//                ) {
//                    composable("detail"){
//                        cardDetail()
//                    }
//
//                }
        }
    }


    @Composable
    fun cardDetail() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "카드뷰 상세")
        }
    }

    @Composable
    fun DropDown(
        text: String,
        modifier: Modifier = Modifier,
        initiallyOpened: Boolean = false,
        content: @Composable () -> Unit
    ) {
        val isOpen = remember {
            mutableStateOf(initiallyOpened)
        }

//        val alpha = animateFloatAsState(
//            targetValue = if (isOpen.value) 1f else 0f,
//            animationSpec = tween(
//                durationMillis = 300
//            ), label = ""
//        )
//        val rotateX = animateFloatAsState(
//            targetValue = if (isOpen.value) 0f else -90f,
//            animationSpec = tween(
//                durationMillis = 300
//            ), label = ""
//        )
        val expanded = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .background(color = Color.Blue)
                .animateContentSize()
                .height(if (expanded.value) 400.dp else 200.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded.value = !expanded.value
                }

        ) {
        }
    }

    @Composable
    fun LinearDeterminateIndicator() {
        var currentProgress = remember { mutableStateOf(0.5f) }
        var loading = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope() // Create a coroutine scope
        LinearProgressIndicator(
            progress = { currentProgress.value },
            modifier = Modifier.fillMaxWidth(),
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
    }

    /** Iterate the progress value */
    suspend fun loadProgress(updateProgress: (Float) -> Unit) {
        for (i in 1..100) {
            updateProgress(i.toFloat() / 100)
            delay(100)
        }
    }





    @Preview
    @Composable
    fun CardActivityPreview() {
    }
}
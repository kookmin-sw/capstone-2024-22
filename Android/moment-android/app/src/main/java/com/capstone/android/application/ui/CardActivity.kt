package com.capstone.android.application.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.capstone.android.application.R
import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.app.composable.FancyProgressBar
import com.capstone.android.application.app.composable.convertUrlLinkStringToRcorderNameString
import com.capstone.android.application.app.utile.MomentPath
import com.capstone.android.application.app.utile.loading.GlobalLoadingDialog
import com.capstone.android.application.app.utile.loading.LoadingState
import com.capstone.android.application.app.utile.recorder.MomentAudioPlayer
import com.capstone.android.application.app.utile.recorder.MomentAudioRecorder
import com.capstone.android.application.domain.Card
import com.capstone.android.application.domain.Emotion
import com.capstone.android.application.presentation.CardViewModel
import com.capstone.android.application.presentation.DownloadLinkViewModel
import com.capstone.android.application.ui.theme.FontMoment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import androidx.compose.ui.Alignment.Companion as Alignment1

@AndroidEntryPoint
class CardActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    private val cardViewModel: CardViewModel by viewModels()
    private val downloadLinkViewModel : DownloadLinkViewModel by viewModels()
    @Inject lateinit var momentAudioPlayer:MomentAudioPlayer

    private val recorder by lazy {
        MomentAudioRecorder(applicationContext)
    }

    private val player by lazy {
        MomentAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    private lateinit var mainIntent : Intent
    private var tripFileListIndex:Int = -1
    private var totalCount:Int = 0




    override fun onStop() {
        super.onStop()
        momentAudioPlayer.stop()
        recorder.stop()
    }

    @Composable
    fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
        val eventHandler = rememberUpdatedState(onEvent)
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { owner, event ->
                eventHandler.value(owner, event)
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this@CardActivity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        this.onBackPressedDispatcher.addCallback{
            mainIntent.putExtra("tripFileListIndex",tripFileListIndex)
            mainIntent.putExtra("totalCount",totalCount)
            setResult(1,mainIntent)
            finish()
        }
        mainIntent = intent
        setContent {
            val cardItems = remember {
                mutableStateListOf<Card>()
            }

            val deleteCardIdList = remember {
                mutableStateListOf<Int>()
            }

            val emotionList = remember {
                mutableStateListOf<Emotion>()
            }


            val lifecycleOwner = LocalLifecycleOwner.current
            OnLifecycleEvent{ lifecycleOwner,event->
                when(event){
                    Lifecycle.Event.ON_CREATE -> {}
                    Lifecycle.Event.ON_START -> {}
                    Lifecycle.Event.ON_RESUME -> {}
                    Lifecycle.Event.ON_PAUSE -> {}
                    Lifecycle.Event.ON_STOP -> {
                        Log.d("awegwaegwe","start")
//                        cardItems.filter { it.uploadImage.isNotEmpty() }.forEach {card->
//
//                            cardViewModel.postCardImageUpload(
//                                cardViewId = card.cardView.id,
//                                uploadImageList = card.uploadImage.map{ file->
//                                    MultipartBody.Part.createFormData(
//                                        name = "images",
//                                        filename = file.name,
//                                        body = file.asRequestBody("image/*".toMediaType())
//                                    )
//                                } as ArrayList<MultipartBody.Part>
//                            )
//                        }
                    }
                    Lifecycle.Event.ON_DESTROY -> {}
                    Lifecycle.Event.ON_ANY -> {}
                }
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
                    text = "우울해요",
                    persent = 0f,
                    color = "#1F9854"
                )
            )

            emotionList.add(
                Emotion(
                    icon = R.drawable.ic_emotion_sad,
                    text = "역겨워요",
                    persent = 0f,
                    color = "#030712"
                )
            )




            val tripFileId = remember {
                mutableStateOf(0)
            }
            try {
                mainIntent.let {
                    tripFileId.value = it.getIntExtra("tripFileId", -1)
                    tripFileListIndex = it.getIntExtra("tripFileListIndex",-1)

                    if(tripFileId.value == -1 || tripFileListIndex == -1){
                        throw java.lang.Exception()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@CardActivity, "server error", Toast.LENGTH_SHORT).show()
                finish()
            }


                var isEdit = remember { mutableStateOf(false) }

                cardViewModel.getCardAll(
                    tripFileId = tripFileId.value
                )


                cardViewModel.deleteCardSuccess.observe(this@CardActivity){ response->
                    deleteCardIdList.removeFirst()
                    if(deleteCardIdList.isEmpty()){
                        LoadingState.hide()
                        cardViewModel.getCardAll(
                            tripFileId = tripFileId.value
                        )
                    }


                }

                cardViewModel.getCardAllSuccess.observe(this@CardActivity) { response ->
                    cardItems.clear()
                    response.data.cardViews.mapNotNull { cardView ->
                        runCatching {

                            Card(
                                cardView = cardView,
                                uploadImage = ArrayList<File>(),
                                imageNum = mutableStateOf(0)
                            ).apply {
                                this.isFavorite.value = cardView.loved
                            }


                        }.onSuccess {}.onFailure {}.getOrNull()
                    }.forEach {
                        if(File(convertUrlLinkStringToRcorderNameString(it.cardView.recordFileUrl)).exists()){

                        }else{
                            downloadLinkViewModel.downloadFileFromUrl(
                                url = it.cardView.recordFileUrl
                            )
                        }

                        cardItems.add(it)
                    }
                    if(!cardItems.isNullOrEmpty()){
                        cardItems.let {
                            emotionList[0].persent = it.first().cardView.neutral.toFloat()*(0.01f)
                            emotionList[1].persent = it.first().cardView.happy.toFloat()*(0.01f)
                            emotionList[2].persent = it.first().cardView.angry.toFloat()*(0.01f)
                            emotionList[3].persent = it.first().cardView.sad.toFloat()*(0.01f)
                            emotionList[4].persent = it.first().cardView.disgust.toFloat()*(0.01f)
                        }
                    }
                    totalCount = cardItems.size

                }

                cardViewModel.getCardAllFailure.observe(this@CardActivity) { error ->

                }
                cardViewModel.postCardImageUploadSuccess.observe(this@CardActivity){response->
                    LoadingState.hide()
                }




                Scaffold(
                    modifier = Modifier
                        .fillMaxWidth(),
                    topBar = {
                        GlobalLoadingDialog()
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
                                                mainIntent.putExtra("tripFileListIndex",tripFileListIndex)
                                                mainIntent.putExtra("totalCount",totalCount)
                                                setResult(1,mainIntent)
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
                                                cardItems.filter { it.isDelete.value }.forEach {
                                                    deleteCardIdList.add(it.cardView.id)
                                                }
                                                if(isEdit.value && deleteCardIdList.isNotEmpty()){
                                                    LoadingState.show()
                                                    deleteCardIdList.forEach {
                                                        cardViewModel.deleteCard(
                                                            cardViewId = it
                                                        )
                                                    }
                                                }else{
                                                    cardItems.map { it.isDelete.value = false }
                                                }
                                                isEdit.value = !isEdit.value

                                            },
                                        text = if(isEdit.value) "삭제" else "편집",
                                        fontFamily = FontMoment.obangFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if(isEdit.value) Color("#99342E".toColorInt()) else Color.Black
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
                        Main(isEdit = isEdit, cardItems = cardItems, emotionList = emotionList)
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





        @OptIn(ExperimentalGlideComposeApi::class)
        @Composable
        fun Main(isEdit: MutableState<Boolean>, cardItems: MutableList<Card>, emotionList:MutableList<Emotion>) {
            var expanded = remember { mutableStateOf(true) }

            val imageList = mutableStateListOf<File>()
            val cardImages=ArrayList<MultipartBody.Part>()
            val focusedCardIndex = remember {
                mutableStateOf(0)
            }

            val focusedCardImageNum = remember {
                mutableStateOf(0)
            }
            val maxUploadImages = remember {
                mutableStateOf(2)
            }
            focusedCardImageNum.value = if(cardItems.isNullOrEmpty()) 0 else cardItems[focusedCardIndex.value].let { it.uploadImage.size+it.cardView.imageUrls.size }


            maxUploadImages.value = if(10-focusedCardImageNum.value==0) 2 else 10-focusedCardImageNum.value


            val pickMultipleMedia = rememberLauncherForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(maxUploadImages.value)
            ) { uris ->

                if (uris.isNotEmpty()) {
                    uris.forEach {
                        Log.d("awegwaeg", it.path.toString())

                        val file = File(getPath(it))
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            name = "roomImages",
                            filename = file.name,
                            body = file.asRequestBody("image/*".toMediaType())
                        )

                        cardItems[focusedCardIndex.value].uploadImage.add(file)
                    }
                    cardItems[focusedCardIndex.value].imageNum.value = cardItems[focusedCardIndex.value].uploadImage.size

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }


                cardItems.filter { it.uploadImage.isNotEmpty() }.forEach { card->
                    if(!LoadingState.isLoading.value) LoadingState.show()

                    cardViewModel.postCardImageUpload(
                        cardViewId = card.cardView.id,
                        uploadImageList = card.uploadImage.map{ file->
                            MultipartBody.Part.createFormData(
                                name = "images",
                                filename = file.name,
                                body = file.asRequestBody("image/*".toMediaType())
                            )
                        } as ArrayList<MultipartBody.Part>
                    )
                }
            }

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

                                            cardItems[index].isDelete.value =
                                                !cardItems[index].isDelete.value
                                            if (!cardItems[index].isDelete.value) {
                                                cardItems.forEach {
                                                    it.isDelete.value = false
                                                }
                                            }
                                        },
                                    painter = painterResource(id = if (cardItems[index].isDelete.value) R.drawable.ic_box_check else R.drawable.ic_box_uncheck),
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
                                        momentAudioPlayer.stop()
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
                                        Image(
                                            modifier = Modifier
                                                .clickable {
                                                    cardItems[index].isFavorite.value =
                                                        !cardItems[index].isFavorite.value
                                                    cardViewModel.putCardLike(
                                                        cardViewId = cardItems[index].cardView.id
                                                    )
                                                },
                                            painter = painterResource(id = if (cardItems[index].isFavorite.value) R.drawable.ic_heart_red else R.drawable.ic_heart_white),
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
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
                                            verticalAlignment = Alignment1.CenterVertically
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
                                                        val file = File(MomentPath.RECORDER_PATH+convertUrlLinkStringToRcorderNameString(cardItems[index].cardView.recordFileUrl))
                                                        if(file.exists()){
                                                            if(!momentAudioPlayer.checkSameFile(file.name)){
                                                                momentAudioPlayer.playFile(file)
                                                            }else if(momentAudioPlayer.isIng()){
                                                                momentAudioPlayer.pause()
                                                            }else{
                                                                momentAudioPlayer.start()
                                                            }
                                                        }else{
                                                            Toast.makeText(this@CardActivity,"녹음파일이 없습니다.",Toast.LENGTH_SHORT).show()
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
                                        if(cardItems[index].cardView.imageUrls.isNullOrEmpty() && cardItems[index].imageNum.value==0) {

                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 24.dp)
                                                    .clickable {
                                                        focusedCardIndex.value = index
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
                                                    ,
                                                contentPadding = PaddingValues(horizontal = 32.dp),
                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.CenterVertically,
                                                content = {
                                                    items(cardItems[index].cardView.imageUrls.size) { imageUrlindex ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(72.dp)
                                                        ) {

                                                            GlideImage(
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(70.dp)
                                                                    .clip(RoundedCornerShape(6.dp)),
                                                                model = cardItems[index].cardView.imageUrls[imageUrlindex],
                                                                contentDescription = "Image",
                                                            )


                                                        }

                                                        Spacer(modifier = Modifier.width(8.dp))
                                                    }


                                                    items(cardItems[index].imageNum.value){uploadImageIndex->
                                                        AsyncImage(
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(70.dp)
                                                                    .clip(RoundedCornerShape(6.dp)),
                                                                model = ImageRequest.Builder(this@CardActivity)
                                                                    .data(cardItems[index].uploadImage[uploadImageIndex])
                                                                    .build(),
                                                                contentDescription = "image"
                                                            )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                    }

                                                    if(cardItems[index].let { it.cardView.imageUrls.size + it.uploadImage.size } < 10){
                                                        item {
                                                            Image(
                                                                modifier = Modifier.clickable {
                                                                    focusedCardIndex.value = index
                                                                    pickMultipleMedia.launch(
                                                                        PickVisualMediaRequest(
                                                                            ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                                                        )
                                                                    )
                                                                },
                                                                painter = painterResource(R.drawable.ic_plus_image),
                                                                contentDescription = "image plus"
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
                                                text = when(index){
                                                    0 -> {"꽤나 즐거운 대화였네요"}
                                                    1 -> {"오늘도 고생했어요"}
                                                    2 -> {"좋은 하루 보내세요"}
                                                    else -> {"푹 쉬세요"}
                                                },
                                                fontFamily = FontMoment.preStandardFont,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = "감정분석",
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
                                                        modifier = Modifier.weight(1f),
                                                        color = Color(item.color.toColorInt()),
                                                        progress = {
                                                            when(item.text){
                                                                "평범해요" -> {
                                                                    cardItems[index].cardView.neutral.let {
                                                                        it.toFloat()*(0.01f)
                                                                    }
                                                                }
                                                                "즐거워요" -> {
                                                                    cardItems[index].cardView.happy.let {
                                                                        it.toFloat()*(0.01f)
                                                                    }
                                                                }
                                                                "화가나요" -> {
                                                                    cardItems[index].cardView.angry.let {
                                                                        it.toFloat()*(0.01f)
                                                                    }
                                                                }
                                                                "슬퍼요 ." -> {
                                                                    cardItems[index].cardView.sad.let {
                                                                        it.toFloat()*(0.01f)
                                                                    }
                                                                }
                                                                else -> {
                                                                    cardItems[index].cardView.disgust.let {
                                                                        it.toFloat()*(0.01f)
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    )
                                                    Spacer(modifier = Modifier.width(36.dp))

                                                    Text(
                                                        modifier = Modifier.weight(0.3f),
                                                        text = "${(item.persent*100).toInt()}%",
                                                        fontFamily = FontMoment.preStandardFont,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }

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
            val isEdit: MutableState<Boolean> = remember {
                mutableStateOf(false)
            }
            val cardItems: MutableList<Card> = remember {
                mutableStateListOf<Card>()
            }
//            for (i in 0 until 3) {
//                cardItems.add(
//                    Card(
//                        cardView = CardView(
//                            angry = 1.0, happy = 1.0, id = 1, location = "", loved = false,
//                            neutral = 1.0, question = "", recordFileLength = 1, recordFileName = "",
//                            recordFileStatus = "", recordFileUrl = "", recordedAt = "",
//                            sad = 1.0, stt = "", temperature = "", tripFileId = 1, weather = "",
//                            disgust = 1.0, imageUrls = listOf("weag")
//                        )
//                    )
//                )
//            }

//            Main(isEdit = isEdit, cardItems = cardItems)
        }
    }
    

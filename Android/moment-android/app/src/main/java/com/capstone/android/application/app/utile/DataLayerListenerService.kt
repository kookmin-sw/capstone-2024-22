package com.capstone.android.application.app.utile

import android.os.Build
import android.util.Log
import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.app.utile.common.GetWeatherType
import com.capstone.android.application.app.utile.location.MomentLocation
import com.capstone.android.application.data.remote.card.CardRepository
import com.capstone.android.application.data.remote.card.model.card_post.request.PostCardUploadReqeust
import com.capstone.android.application.data.remote.kakao.KakaoRepository
import com.capstone.android.application.data.remote.open_weather.OpenWeatherRepository
import com.capstone.android.application.data.remote.trip.TripRepository
import com.capstone.android.application.domain.response.ApiResponse
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "DataLayerSample"
private const val START_ACTIVITY_PATH = "/start-activity"
private const val DATA_ITEM_RECEIVED_PATH = "/data-item-received"

@AndroidEntryPoint
class DataLayerListenerService: WearableListenerService() {
    private val VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription"
    @Inject lateinit var tripRepository: TripRepository
    @Inject lateinit var cardRepository: CardRepository
    @Inject lateinit var openWeatherRepository: OpenWeatherRepository
    @Inject lateinit var kakaoRepository:KakaoRepository
    @Inject lateinit var momentLocation: MomentLocation


    override fun onDataChanged(dataEvents: DataEventBuffer) {
//        if (Log.isLoggable(TAG, Log.DEBUG)) {
//            Log.d(TAG, "onDataChanged: $dataEvents")
//        }

        Log.d("waegeawgewag","wegws")

        // Loop through the events and send a message
        // to the node that created the data item.
        dataEvents.map { it.dataItem.uri }
            .forEach { uri ->
                // Get the node ID from the host value of the URI.
                val nodeId: String = uri.host.toString()
                // Set the data of the message to be the bytes of the URI.
                val payload: ByteArray = uri.toString().toByteArray()

                // Send the RPC.
                Wearable.getMessageClient(this)
                    .sendMessage(nodeId, DATA_ITEM_RECEIVED_PATH, payload)
            }
    }

    override fun onMessageReceived(message: MessageEvent) {
        super.onMessageReceived(message)
        saveFile(message.data,MomentPath.RECORDER_PATH,"warch.mp3")


        momentLocation.getLocation().invoke().apply {
            this.addOnSuccessListener {
                Log.d("waegwaegewag","${it.latitude}, ${it.longitude}")
                runBlocking {
                    var addressName:String=""
                    var currentDate:String = ""
                    var weather:String=""
                    var temp:String=""

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        currentDate = "${LocalDateTime.now()}"
                    } else {
                        val date = Date(System.currentTimeMillis())
                        val dateFormat = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            Locale.KOREA
                        )
                        currentDate=dateFormat.format(date)
                    }



                    val test = kakaoRepository.getLocal(x= it.longitude.toString(), y=it.latitude.toString())

                    if(test is ApiResponse.Success){
                        addressName = test.data.documents.first().address_name
                    }

                    val test2 = openWeatherRepository.getWeatherInCurrentLocation(
                        lat = it.latitude.toString(),
                        lon = it.longitude.toString(),
                        appid = ApplicationClass.openWeartherAppId
                    )
                    if(test2 is ApiResponse.Success){
                        weather = GetWeatherType(test2.data.weather.first().main)
                        temp = test2.data.main.temp.toFloat().minus(273.15).toInt().toString()
                    }
                    val audioFile = File(MomentPath.RECORDER_PATH+"warch.mp3")
                    if(audioFile.exists()){
                        val requestFile = audioFile?.asRequestBody("audio/*".toMediaTypeOrNull())

                        val body = MultipartBody.Part.createFormData("recordFile",audioFile?.name,
                            requestFile!!
                        )

                        val cardUploadDto= Gson().toJson(
                            PostCardUploadReqeust(
                                location = addressName,
                                question = "",
                                recordedAt = currentDate,
                                temperature = temp,
                                weather = weather
                            )
                        )

                        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
                        val gson = Gson()
                        val data = gson.toJson(cardUploadDto)
                        val requestbody = RequestBody.create(JSON, cardUploadDto)
                        Log.d("weagewagwegweag","hello")
                        cardRepository.postCardUpload(
                            cardUploadMultipart = requestbody,
                            recordFile = body
                        )
                    }else{
                        Log.d("waegaweg","world")
                    }


                }


            }
        }

    }


    fun saveFile(body: ByteArray?, pathWhereYouWantToSaveFile: String, fileName:String):String{

        if (body==null)
            return ""
        var input: InputStream? = null

        try {
            val newFolder: File = File(pathWhereYouWantToSaveFile)
            if(!newFolder.exists()){
                newFolder.mkdir()
            }

            input = body.inputStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile+fileName)

            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
        }
        finally {
            input?.close()
        }
        return ""
    }

}
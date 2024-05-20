package com.capstone.android.application.app.utile.firebase

import android.content.Intent
import android.util.Log
import com.capstone.android.application.app.ApplicationClass.Companion.transactionId
import com.capstone.android.application.app.utile.notification.MomentNotificationService
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.Wearable
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var momentNotificationService: MomentNotificationService

    private val VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //token을 서버로 전송
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var title:String = ""
        var content:String = ""


        try {

            remoteMessage.data.get("title")?.also {
                title = it
            }

            remoteMessage.data.get("body")?.also {
                content = it
            }
        }catch (e:Exception){

        }
        if(title.isNullOrEmpty() || content.isNullOrEmpty()){
            title= remoteMessage.notification?.title.toString()
            content = remoteMessage.notification?.body.toString()
        }



        requestTranscription(content.toByteArray(),transcriptionNodeId = transactionId)

        momentNotificationService.showBasicNotification(
            title = title,
            content = content
        )


        //수신한 메시지를 처리
    }



    private fun requestTranscription(voiceData: ByteArray,transcriptionNodeId:String) {
        Log.d("waegweaga","wegwe")
        transcriptionNodeId?.also { nodeId ->
            val sendTask: Task<*> = Wearable.getMessageClient(this).sendMessage(
                nodeId,
                VOICE_TRANSCRIPTION_MESSAGE_PATH,
                voiceData
            ).apply {
                addOnSuccessListener {
                    Log.d("waegaewgew","send data success")
                }
                addOnFailureListener {
                    Log.d("waegaewgew","${it.message}")

                }
            }
        }
    }

}
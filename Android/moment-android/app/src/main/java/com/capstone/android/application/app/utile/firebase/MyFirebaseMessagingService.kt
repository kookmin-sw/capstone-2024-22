package com.capstone.android.application.app.utile.firebase

import android.content.Intent
import com.capstone.android.application.app.utile.notification.MomentNotificationService
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var momentNotificationService: MomentNotificationService

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



        momentNotificationService.showBasicNotification(
            title = title,
            content = content
        )


        //수신한 메시지를 처리
    }

    override fun handleIntent(intent: Intent?) {

        val new = intent?.apply {
            val temp = extras?.apply {
                remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
//                remove(keyWithOldPrefix(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION))
            }
            replaceExtras(temp)
        }
        super.handleIntent(new)
    }


}
package com.capstone.android.application.app.utile.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.ui.SplashActivity
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlin.random.Random


@Module
@InstallIn(SingletonComponent::class)
class MomentNotificationService @Inject constructor(@ApplicationContext private val context:Context){
    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    fun showBasicNotification(title:String,content:String){
        val notifyIntent = Intent(context,  MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification= NotificationCompat.Builder(context,"moment_notification")
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(notifyPendingIntent)
            .setSmallIcon(R.drawable.moment_app_icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )



    }

    fun showExpandableNotification(){
        val notification=NotificationCompat.Builder(context,"moment_notification")
            .setContentTitle("Water Reminder")
            .setContentText("Time to drink a glass of water")
            .setSmallIcon(R.drawable.moment_app_icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)

            .build()
        notificationManager.notify(Random.nextInt(),notification)
    }

}
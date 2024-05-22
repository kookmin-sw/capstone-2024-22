package com.capstone.android.application.app.utile.recorder

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class MomentAudioPlayer @Inject constructor(
    @ActivityContext private val context: Context
) : AudioPlayer {

    private var player : MediaPlayer?=null
    private var isIng :Boolean = false
    private var isCreated:Boolean = false
    private var fileName:String=""
    override fun playFile(file: File) {
        MediaPlayer.create(context,file.toUri()).apply {
            player = this
//            start()
        }
        isCreated = true
//        isIng = true
        fileName = file.name
    }

    fun pause(){
        player?.pause()
        isIng = false
    }

    fun start(){
        player?.start()
        isIng = true

    }

    override fun stop() {
//        player?.stop()
        player?.release()
//        player = null
        isIng = false
        isCreated = false
    }

    fun isActivity(): Boolean {
        try {
            Log.d("weagwaegwe","ewgweg")
            return player!!.isPlaying

        }catch (e:Exception){
            Log.d("weagwaegwe","${e.message}")
            return false
        }
    }


    fun checkSameFile(fileName:String):Boolean{
        return fileName == this@MomentAudioPlayer.fileName
    }

    fun getTimeDuration():String{
        try {
            val duration: Long = player!!.getDuration().toLong()
            val time = java.lang.String.format(
                "%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            )
            Log.d("awegew","${time}, aaa")
            return time

        }catch (e:Exception){
            Log.d("awegew","${e.message}")
            return "0"
        }
    }

    fun getSecondDuration():Int{
        try {
            val duration: Long = player!!.getDuration().toLong()
            val time = TimeUnit.MILLISECONDS.toSeconds(duration).toInt()
            Log.d("awegagwea",time.toString())
            return time

        }catch (e:Exception){
            Log.d("awegagwea","${e.message}")
            return 0
        }
    }



    fun isIng() = isIng

}
package com.capstone.android.application.app.utile.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class MomentAudioRecorder @Inject constructor(
    @ActivityContext private val context : Context
): AudioRecorder {
    private var recorder:MediaRecorder?=null
    private var isIng = false

    private fun createRecorder() :MediaRecorder{
        return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            MediaRecorder(context)
        }else {
            MediaRecorder()
        }
    }
    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this@apply
        }
        isIng=true
    }



    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
        isIng=false
    }

    override fun pause() {
        recorder?.pause()
        isIng=false
    }

    override fun restart() {
        recorder?.resume()
        isIng=true
    }

    fun isActivity() = isIng
}
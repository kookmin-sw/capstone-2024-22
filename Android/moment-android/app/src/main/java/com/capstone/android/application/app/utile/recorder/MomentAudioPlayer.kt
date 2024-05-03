package com.capstone.android.application.app.utile.recorder

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class MomentAudioPlayer @Inject constructor(
    @ActivityContext private val context: Context
) : AudioPlayer {

    private var player : MediaPlayer?=null
    override fun playFile(file: File) {
        MediaPlayer.create(context,file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}
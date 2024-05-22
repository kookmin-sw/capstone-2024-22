package com.capstone.android.application.app.utile.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile : File)
    fun stop()

    fun pause()
    fun restart()
}
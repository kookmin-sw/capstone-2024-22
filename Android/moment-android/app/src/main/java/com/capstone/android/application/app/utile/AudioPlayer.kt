package com.capstone.android.application.app.utile

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}
package com.capstone.android.application.data.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRemote {

    val userMessage :Flow<List<String>> = flow{
        while(true){
            val testArray = ArrayList<String>()
            testArray.add("one")
            testArray.add("two")
            emit(testArray)
            delay(100)
        }

    }

}
package com.capstone.android.application.data.local

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestLocal {
    private fun addUser(){
//        db = MomentRecordDatabase.getInstance(context = applicationContext)!!
        var name = "name"
        var age = "1"
        var phone = "214124"

        CoroutineScope(Dispatchers.IO).launch {
//            db.recordDao().insert(MomentRecord(name = name, age = age, phone = phone))
        }
    }
}
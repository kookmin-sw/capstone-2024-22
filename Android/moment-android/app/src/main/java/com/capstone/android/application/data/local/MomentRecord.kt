package com.capstone.android.application.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MomentRecord(
    var name: String,
    var age:String,
    var phone:String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
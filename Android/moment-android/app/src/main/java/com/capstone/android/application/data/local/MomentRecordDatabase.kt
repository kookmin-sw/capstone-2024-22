package com.capstone.android.application.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MomentRecord::class], version = 1)
abstract class MomentRecordDatabase: RoomDatabase() {
    abstract fun recordDao(): MomentRecordDao

    companion object {
        private var instance: MomentRecordDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MomentRecordDatabase? {
            if (instance == null) {
                synchronized(MomentRecordDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MomentRecordDatabase::class.java,
                        "moment-database"
                    ).build()
                }
            }
            return instance
        }
    }
}
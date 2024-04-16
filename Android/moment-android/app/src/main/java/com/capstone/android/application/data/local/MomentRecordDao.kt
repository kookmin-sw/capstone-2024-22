package com.capstone.android.application.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MomentRecordDao {
    @Insert
    fun insert(record: MomentRecord)

    @Update
    fun update(record: MomentRecord)

    @Delete
    fun delete(record: MomentRecord)

    @Query("SELECT * FROM MomentRecord") // 테이블의 모든 값을 가져와라
    fun getAll(): List<MomentRecord>
}

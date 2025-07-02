package com.tanveer.eventplannerproject.Schedule

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScheduleDaoInterface {
    @Insert
    fun insertScheduleDetail(scheduleDataClass: ScheduleDataClass)

    @Query("SELECT * FROM ScheduleDataClass where eventId= :eventId")
    fun getScheduleListOf(eventId: Int): List<ScheduleDataClass>

    @Update
    fun update(scheduleDataClass: ScheduleDataClass)

    @Delete
    fun delete(scheduleDataClass: ScheduleDataClass)

    @Query("SELECT * FROM ScheduleDataClass")
    fun getList(): List<ScheduleDataClass>
}
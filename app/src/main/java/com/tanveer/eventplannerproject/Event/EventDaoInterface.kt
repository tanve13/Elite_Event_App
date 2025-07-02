package com.tanveer.eventplannerproject.Event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tanveer.eventplannerproject.Event.EventDataClass

@Dao
interface EventDaoInterface {
    @Insert
    fun insertEventDetail(eventDataClass: EventDataClass)

    @Update
    fun update(eventDataClass: EventDataClass)

    @Delete
    fun delete(eventDataClass: EventDataClass)

    @Query("SELECT * FROM EventDataClass")
    fun getList(): List<EventDataClass>

    @Query("SELECT * FROM EventDataClass WHERE dateTime BETWEEN :startDate AND :endDate")
    fun getTaskBetweenDates(startDate: String, endDate: String):List<EventDataClass>

}
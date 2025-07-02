package com.tanveer.eventplannerproject.Schedule

import androidx.room.TypeConverter
import java.util.Date

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?) : Date?{
        return value?.let{ Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long?{
        return date?.time?.toLong()
    }
}
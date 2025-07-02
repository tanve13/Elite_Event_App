package com.tanveer.eventplannerproject.Schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tanveer.eventplannerproject.Event.EventDataClass
import java.util.Calendar
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EventDataClass::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduleDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var eventId: Int? = 0,
    var note: String? = "",
    var createdDate: Date? = Calendar.getInstance().time,
    var createdTime: Date? = Calendar.getInstance().time

)

package com.tanveer.eventplannerproject.Event

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity
data class EventDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String? = "",
    var note: String? = "",
    var clientName: String? = "",
    var clientPhoneNumber: String? = "",
    var clientAddress: String? = "",
    var isCompleted: Boolean? = false,
    var dateTime : Date?= Calendar.getInstance().time,
    var time: Date?= Calendar.getInstance().time

)



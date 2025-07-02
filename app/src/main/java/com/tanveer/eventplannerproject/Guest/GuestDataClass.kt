package com.tanveer.eventplannerproject.Guest

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tanveer.eventplannerproject.Event.EventDataClass

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
data class GuestDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var eventId: Int? = 0,
    var GuestName: String? = "",
    var person: Int = 0 ,
    var Group: String? = "",
    var Menu: String? = "",
    var Table: String?= "",
    var Number: String? = "",
    var tvNote: String? = "",
    var tvEmail: String? = "",
    var tvAddress: String? = "",
    var send: Int? = 0
)

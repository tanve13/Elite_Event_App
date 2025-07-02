package com.tanveer.eventplannerproject.Event

import androidx.room.Embedded
import androidx.room.Relation
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.Guest.GuestDataClass

data class EventList(
    @Embedded
    var eventDataClass: EventDataClass,
    @Relation(entity = GuestDataClass::class,
        parentColumn = "id",
        entityColumn = "guestId")
    var todo: List<GuestDataClass>
)

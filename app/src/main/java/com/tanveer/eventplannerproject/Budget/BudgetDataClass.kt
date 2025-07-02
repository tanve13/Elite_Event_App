package com.tanveer.eventplannerproject.Budget

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
data class BudgetDataClass(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val eventId: Int? = 0,
    var name: String? = "",
    var Note: String? = "",
    var category: String? = "",
    var amount: String? = ""
)

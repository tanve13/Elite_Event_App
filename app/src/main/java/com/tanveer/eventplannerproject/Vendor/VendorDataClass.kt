package com.tanveer.eventplannerproject.Vendor

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
data class VendorDataClass(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var eventId: Int? = 0,
    var tvVendorName: String? = "",
    var tvVendorNote: String? = "",
    var vendorCategory: String? = "",
    var contact: String? = "",
    var tvEmail: String? = "",
    var tvAddress: String? = "",
    var status: Int = 0
)

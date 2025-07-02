package com.tanveer.eventplannerproject.Guest

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GuestDaoInterface {
    @Insert
    fun insertGuestDetail(guestDataClass: GuestDataClass)

    @Query("SELECT * FROM GuestDataClass where eventId= :eventId")
    fun getGuestListOf(eventId: Int): List<GuestDataClass>

    @Update
    fun updateGuestList(guestDataClass: GuestDataClass)

    @Delete
    fun deleteGuestList(guestDataClass: GuestDataClass)

    @Query("SELECT * FROM GuestDataClass")
    fun getGuestList(): List<GuestDataClass>
}
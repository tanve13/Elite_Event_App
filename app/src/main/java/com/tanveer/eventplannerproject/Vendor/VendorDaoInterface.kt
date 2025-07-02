package com.tanveer.eventplannerproject.Vendor

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VendorDaoInterface {
    @Insert
    fun insertVendorDetail(vendorDataClass: VendorDataClass)

    @Query("SELECT * FROM VendorDataClass where eventId= :eventId")
    fun getVendorListOf(eventId: Int): List<VendorDataClass>

    @Update
    fun update(vendorDataClass: VendorDataClass)

    @Delete
    fun delete(vendorDataClass: VendorDataClass)

    @Query("SELECT * FROM VendorDataClass")
    fun getList(): List<VendorDataClass>
}
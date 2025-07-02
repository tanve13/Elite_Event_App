package com.tanveer.eventplannerproject.Event

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tanveer.eventplannerproject.Budget.BudgetDaoInterface
import com.tanveer.eventplannerproject.Budget.BudgetDataClass
import com.tanveer.eventplannerproject.Schedule.DateTimeConverter
import com.tanveer.eventplannerproject.Guest.GuestDaoInterface
import com.tanveer.eventplannerproject.Guest.GuestDataClass
import com.tanveer.eventplannerproject.Schedule.ScheduleDaoInterface
import com.tanveer.eventplannerproject.Schedule.ScheduleDataClass
import com.tanveer.eventplannerproject.Vendor.VendorDaoInterface
import com.tanveer.eventplannerproject.Vendor.VendorDataClass

@TypeConverters(DateTimeConverter::class)
@Database(
    entities = [EventDataClass::class,
        GuestDataClass::class,
        BudgetDataClass::class, VendorDataClass::class,
        ScheduleDataClass::class],
    version = 1, exportSchema = true
)
abstract class EventDataBase : RoomDatabase() {

    abstract fun eventDao(): EventDaoInterface
    abstract fun guestDao(): GuestDaoInterface
    abstract fun budgetDao(): BudgetDaoInterface
    abstract fun vendorDao(): VendorDaoInterface
    abstract fun scheduleDao(): ScheduleDaoInterface

    companion object {
        private var eventDataBase: EventDataBase? = null

        fun getInstance(context: Context): EventDataBase {
            if (eventDataBase == null) {
                eventDataBase = Room.databaseBuilder(
                    context,
                    EventDataBase::class.java,
                    "EventDataBase"
                ).addCallback(object: Callback(){
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.setForeignKeyConstraintsEnabled(true)
                    }

                }).allowMainThreadQueries()
                    .build()
            }
            return eventDataBase!!
        }
    }
}

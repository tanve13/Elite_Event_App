package com.tanveer.eventplannerproject.Budget

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tanveer.eventplannerproject.Budget.BudgetDataClass

@Dao
interface BudgetDaoInterface {
    @Insert
    fun insertBudgetDetail(budgetDataClass: BudgetDataClass)

    @Query("SELECT * FROM BudgetDataClass where eventId= :eventId")
    fun getBudgetListOf(eventId: Int): List<BudgetDataClass>

    @Update
    fun update(budgetDataClass: BudgetDataClass)

    @Delete
    fun delete(budgetDataClass: BudgetDataClass)

    @Query("SELECT * FROM BudgetDataClass")
    fun getList(): List<BudgetDataClass>
}
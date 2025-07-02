package com.tanveer.eventplannerproject.Event

interface EventInterface {
    fun updateEvent(position: Int)
    fun deleteEvent(position: Int)
    fun itemClick(position: Int)
    fun isCompleted(position: Int, isChecked: Boolean)
}
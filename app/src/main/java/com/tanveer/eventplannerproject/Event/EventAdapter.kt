package com.tanveer.eventplannerproject.Event

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.tanveer.eventplannerproject.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class EventAdapter(var context: Context, var eventList: ArrayList<EventDataClass>,
                   var eventInterface: EventInterface
) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    private val TAG = "adapter"
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvEventName: TextView = view.findViewById(R.id.tvEventName)
        var tvEventId: TextView = view.findViewById(R.id.tvEventId)
        var cbIsCompleted: CheckBox = view.findViewById(R.id.cbIsCompleted)
        var date: TextView = view.findViewById(R.id.date)
        var time: TextView = view.findViewById(R.id.time)
        var tvUpdate: TextView = view.findViewById(R.id.tvUpdate)
        var tvDelete: TextView = view.findViewById(R.id.tvDelete)
        var moveToNext: TextView = view.findViewById(R.id.moveToNext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder:$position")
        holder.tvEventId.setText(eventList[position].id.toString())
        holder.tvEventName.setText(eventList[position].name)
        holder.cbIsCompleted.setOnClickListener {
            eventInterface.isCompleted(position = position, holder.cbIsCompleted.isChecked?:false)
        }
        holder.cbIsCompleted.isChecked = eventList[position].isCompleted?:false

        if(eventList[position].isCompleted == true){
            holder.tvEventName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.date.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.time.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.tvEventName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.date.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.time.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val sharedPreferences : SharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.app_name),Context.MODE_PRIVATE)
        var dateFormat = sharedPreferences.getString("dateTimeFormat","dd/MMM/yyyy").toString()
        holder.date.setText(SimpleDateFormat(dateFormat).format(eventList[position].dateTime))

        var timeFormat = sharedPreferences.getString("timeFormat","hh:mm aa")
        holder.time.setText(SimpleDateFormat(timeFormat).format(eventList[position].time))
        holder.tvUpdate.setOnClickListener {
            eventInterface.updateEvent(position)
        }
        holder.tvDelete.setOnClickListener {
            eventInterface.deleteEvent(position)
        }
        holder.moveToNext.setOnClickListener {
            eventInterface.itemClick(position)
        }
    }
}
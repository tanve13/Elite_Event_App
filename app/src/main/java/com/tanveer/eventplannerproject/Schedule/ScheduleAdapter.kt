package com.tanveer.eventplannerproject.Schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanveer.eventplannerproject.R
import java.text.SimpleDateFormat
import java.util.Calendar

class ScheduleAdapter(var scheduleList:ArrayList<ScheduleDataClass>,
                      var scheduleInterface: ScheduleInterface
):
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var etScheduleNote: TextView = view.findViewById(R.id.tvNote)
        var tvDate: TextView = view.findViewById(R.id.tvDate)
        var tvTime: TextView = view.findViewById(R.id.tvTime)
        var tvUpdate: TextView = view.findViewById(R.id.tvUpdate)
        var tvDelete: TextView = view.findViewById(R.id.tvDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_schedule,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.etScheduleNote.setText(scheduleList[position].note)
//        var createdDate = Calendar.getInstance()
//        createdDate.time = scheduleList[position].createdDate
        holder.tvDate.setText(SimpleDateFormat("dd/MMM/YYYY").format(scheduleList[position].createdDate))
        holder.tvTime.setText(SimpleDateFormat("hh:mm aa").format(scheduleList[position].createdTime))
        holder.tvUpdate.setOnClickListener {
            scheduleInterface.updateSchedule(position)
        }
        holder.tvDelete.setOnClickListener {
            scheduleInterface.deleteSchedule(position)
        }

    }

}
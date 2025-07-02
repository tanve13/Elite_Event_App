package com.tanveer.eventplannerproject.Guest

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tanveer.eventplannerproject.R

class GuestAdapter (var guestList:ArrayList<GuestDataClass>,
                    var guestInterface: GuestInterface
):
    RecyclerView.Adapter<GuestAdapter.ViewHolder>(){
        class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
            var tvGuestName: TextView = view.findViewById(R.id.tvGuest_Name)
            var tvPhoneNumber: TextView = view.findViewById(R.id.tvPhoneNumber)
            var tvEmail: TextView = view.findViewById(R.id.tvEmail)
            var tvstatus: TextView = view.findViewById(R.id.tvStatus)
            var tvUpdate: TextView = view.findViewById(R.id.tvUpdate)
            var tvDelete: TextView = view.findViewById(R.id.tvDelete)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_guest,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return guestList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGuestName.setText(guestList[position].GuestName)
        holder.tvPhoneNumber.setText(guestList[position].Number.toString())
        holder.tvEmail.setText(guestList[position].tvEmail)
        when (guestList[position].person) {
            0 -> {
            }
            1->{
            }
            2 -> {
            }
            3 -> {
            }
        }

        holder.tvstatus.text = when (guestList[position].send) {
            1 -> "Invitation Sent"
            2 -> "Invitation Not Sent"
            else -> "Status Unknown"
        }
        holder.tvUpdate.setOnClickListener {
            guestInterface.updateGuest(position)
        }
        holder.tvDelete.setOnClickListener {
            guestInterface.deleteGuest(position)
        }

    }
}
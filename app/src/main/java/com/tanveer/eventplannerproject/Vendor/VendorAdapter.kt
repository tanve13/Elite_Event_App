package com.tanveer.eventplannerproject.Vendor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanveer.eventplannerproject.R
import org.w3c.dom.Text

class VendorAdapter (var vendorList:ArrayList<VendorDataClass>,
                     var vendorInterface: VendorInterface
):
RecyclerView.Adapter<VendorAdapter.ViewHolder>() {
    class ViewHolder (var view: View) : RecyclerView.ViewHolder(view){
        var tvVendorName: TextView = view.findViewById(R.id.tvVendorName)
        var vendorCategory: TextView = view.findViewById(R.id.tvVendorCategory)
        var contact: TextView = view.findViewById(R.id.contact)
        var tvEmail: TextView = view.findViewById(R.id.tvEmail)
        var tvAddress: TextView = view.findViewById(R.id.tvAddress)
        var rgVendor: RadioGroup = view.findViewById(R.id.rgVendor)
        var tvUpdate: TextView = view.findViewById(R.id.tvUpdate)
        var tvDelete: TextView = view.findViewById(R.id.tvDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_vendor,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return vendorList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvVendorName.setText(vendorList[position].tvVendorName)
        holder.vendorCategory.setText(vendorList[position].vendorCategory)
        holder.contact.setText(vendorList[position].contact.toString())
        holder.tvEmail.setText(vendorList[position].tvEmail)
        holder.tvAddress.setText(vendorList[position].tvAddress)
        holder.rgVendor.checkedRadioButtonId
        when (vendorList[position].status) {
            0 -> {

            }
            1->{
            }
        }
        holder.tvUpdate.setOnClickListener {
            vendorInterface.updateVendor(position)
        }
        holder.tvDelete.setOnClickListener {
            vendorInterface.deleteVendor(position)
        }

    }

}
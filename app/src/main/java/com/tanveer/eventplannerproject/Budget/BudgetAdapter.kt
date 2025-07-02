package com.tanveer.eventplannerproject.Budget

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanveer.eventplannerproject.R

class BudgetAdapter (var budgetList:ArrayList<BudgetDataClass>,
                     var budgetInterface: BudgetInterface
):
    RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {
    private val TAG = "adapter"
    class ViewHolder (var view: View) : RecyclerView.ViewHolder(view){
        var tvName: TextView = view.findViewById(R.id.tvName)
        var category: TextView = view.findViewById(R.id.tvcategory)
        var tvAmount: TextView = view.findViewById(R.id.tvAmount)
        var btnUpdate: TextView = view.findViewById(R.id.tvUpdate)
        var btnDelete: TextView = view.findViewById(R.id.tvDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_budget,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder:$position")
        holder.tvName.setText(budgetList[position].name)
        holder.category.setText(budgetList[position].category)
        holder.tvAmount.setText(budgetList[position].amount.toString())
        holder.btnUpdate.setOnClickListener {
            budgetInterface.updateBudget(position)
        }
        holder.btnDelete.setOnClickListener {
            budgetInterface.deleteBudget(position)
        }

    }

}
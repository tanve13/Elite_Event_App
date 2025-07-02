package com.tanveer.eventplannerproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.eventplannerproject.Budget.BudgetAdapter
import com.tanveer.eventplannerproject.Budget.BudgetDataClass
import com.tanveer.eventplannerproject.Budget.BudgetInterface
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.databinding.DialogBudgetBinding
import com.tanveer.eventplannerproject.databinding.FragmentBudgetBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BudgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetFragment : Fragment(), BudgetInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentBudgetBinding? = null
    lateinit var arrayAdapter: ArrayAdapter<String>
    var categoryArray = arrayOf("Unassigned Category","Attire and Accessories","Health and Beauty",
        "Music and Show","Flower and Decor","Photo and Video","Reception","Transportation","Accommodation")
    lateinit var linearLayoutManager: LinearLayoutManager
    var budgetList = arrayListOf<BudgetDataClass>()
    lateinit var budgetAdapter: BudgetAdapter
    lateinit var eventDataClass: EventDataClass
    var eventDataBase: EventDataBase? = null
    private var TAG = "BudgetFragment"
    var eventId = 0
    var selectedCategory = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetAdapter = BudgetAdapter(budgetList, this)
        eventDataBase = EventDataBase.getInstance(requireContext())
        arguments?.let {
            eventId = it.getInt("eventId")

            Log.e(TAG, "onViewCreated:$eventId")

            getBudgetListof()
        }
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvBudgetList?.layoutManager = linearLayoutManager
        binding?.rvBudgetList?.adapter = budgetAdapter
        binding?.Fab?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = DialogBudgetBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,categoryArray)
            dialogBinding.spCategory.adapter = arrayAdapter
            dialogBinding.spCategory.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoryArray[position]
                    println("SelectedCategory: $selectedCategory")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            dialogBinding.close.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                    dialogBinding.name.error = resources.getString(R.string.enter_name)
                } else if (dialogBinding.amount.text.toString().isNullOrEmpty()) {
                    dialogBinding.amount.error = resources.getString(R.string.Enter_amount)
                } else {
                    eventDataBase?.budgetDao()?.insertBudgetDetail(
                        BudgetDataClass(
                            eventId = eventId,
                            name = dialogBinding.name.text.toString(),
                            Note = dialogBinding.note.text.toString(),
                            category = selectedCategory,
                            amount = dialogBinding.amount.text.toString(),
                        )
                    )
                    Toast.makeText(requireContext(), "Guest details are added", Toast.LENGTH_SHORT)
                    getBudgetListof()
                    dialog.dismiss()

                }
            }
            dialog.show()

        }
    }

    private fun getBudgetListof() {
        budgetList.clear()
        budgetList.addAll(
            eventDataBase?.budgetDao()?.getBudgetListOf(eventId = eventId)?: arrayListOf()
        )
        budgetAdapter.notifyDataSetChanged()
        if (budgetList.size == 0){
            binding?.emptyView?.visibility = View.VISIBLE
        }else{
            binding?.emptyView?.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BudgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun updateBudget(position: Int) {
        var dialog = Dialog(requireContext())
            var dialogBinding = DialogBudgetBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)

            dialogBinding.btnAdd.setText(resources.getString(R.string.Update))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
           dialog.show()
        if (position > -1) {
            dialogBinding.name.setText(budgetList[position].name)
            dialogBinding.note.setText(budgetList[position].Note)
            dialogBinding.category.setText(budgetList[position].category)
            dialogBinding.amount.setText(budgetList[position].amount)

        }
            arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,categoryArray)
            dialogBinding.spCategory.adapter = arrayAdapter
            dialogBinding.spCategory.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoryArray[position]
                    println("SelectedCategory: $selectedCategory")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        dialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                    dialogBinding.name.error = resources.getString(R.string.enter_name)
                } else if (dialogBinding.amount.text.toString().isNullOrEmpty()) {
                    dialogBinding.amount.error = resources.getString(R.string.enter_number)
                } else {
                    eventDataBase?.budgetDao()?.update(
                        BudgetDataClass(
                            eventId = eventId,
                            id = budgetList[position].id ?: 0,
                            name = dialogBinding.name.text.toString(),
                            Note = dialogBinding.note.text.toString(),
                            category = selectedCategory,
                            amount = dialogBinding.amount.text.toString(),
                        )
                    )
                    getBudgetListof()
                  //  budgetAdapter.notifyDataSetChanged()
                   dialog. dismiss()
                }
            }


    }

    override fun deleteBudget(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this))
        alertDialog.setPositiveButton("Yes") { _, _ ->
        //    budgetList.removeAt(position)
            getBudgetListof()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        eventDataBase?.budgetDao()?.delete(
            budgetList[position]
        )

        alertDialog.show()
    }

}
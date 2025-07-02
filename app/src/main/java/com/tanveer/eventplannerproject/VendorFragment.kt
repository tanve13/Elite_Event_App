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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Vendor.VendorAdapter
import com.tanveer.eventplannerproject.Vendor.VendorDataClass
import com.tanveer.eventplannerproject.Vendor.VendorInterface
import com.tanveer.eventplannerproject.databinding.DialogVendorBinding
import com.tanveer.eventplannerproject.databinding.FragmentVendorBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VendorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VendorFragment : Fragment(), VendorInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentVendorBinding? = null
    lateinit var arrayAdapter: ArrayAdapter<String>
    var vendorArray = arrayOf(
        "Unassigned Category",
        "Attire and Accessories",
        "Health and Beauty",
        "Music and Show",
        "Flower and Decor",
        "Photo and Video",
        "Reception",
        "Transportation",
        "Accommodation"
    )
    lateinit var linearLayoutManager: LinearLayoutManager
    var vendorList = arrayListOf<VendorDataClass>()
    lateinit var vendorAdapter: VendorAdapter
    lateinit var eventDataBase: EventDataBase
    private var TAG = "VendorFragment"
    var eventId = 0
    var selectedVendor = ""
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
        binding = FragmentVendorBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vendorAdapter = VendorAdapter(vendorList, this)
        eventDataBase = EventDataBase.getInstance(requireContext())
        arguments?.let {
            eventId = it.getInt("eventId")

            Log.e(TAG, "onViewCreated:$eventId")

            getVendorListOf()
        }
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvVendor?.layoutManager = linearLayoutManager
        binding?.rvVendor?.adapter = vendorAdapter
        binding?.fabButton?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = DialogVendorBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, vendorArray)
            dialogBinding.vendorCategory.adapter = arrayAdapter
            dialogBinding.vendorCategory.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedVendor = vendorArray[position]
                    println("SelectedVendor: $selectedVendor")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            dialogBinding.clickClose.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.addButton.setOnClickListener {
                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                    dialogBinding.name.error = resources.getString(R.string.enter_name)
                } else if (dialogBinding.phone.text.toString().isNullOrEmpty()) {
                    dialogBinding.phone.error = resources.getString(R.string.Enter_PhoneNumber)
                } else if (dialogBinding.rgVendor.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.select_status),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var status = if (dialogBinding.reserved.isChecked) {
                        1
                    } else if (dialogBinding.pending.isChecked) {
                        2
                    } else {
                        0
                    }
                    eventDataBase.vendorDao().insertVendorDetail(
                        VendorDataClass(
                            eventId = eventId,
                            tvVendorName = dialogBinding.name.text.toString(),
                            contact = dialogBinding.phone.text.toString(),
                            tvVendorNote = dialogBinding.note.text.toString(),
                            vendorCategory = selectedVendor,
                            tvEmail = dialogBinding.email.text.toString(),
                            tvAddress = dialogBinding.address.text.toString(),
                            status = status
                        )
                    )
                    Toast.makeText(requireContext(), "Vendor details are added", Toast.LENGTH_SHORT)
                    getVendorListOf()
                    dialog.dismiss()

                }
            }
            dialog.show()


        }

    }


    private fun getVendorListOf() {
        vendorList.clear()
        vendorList.addAll(
            eventDataBase.vendorDao().getVendorListOf(eventId = eventId) ?: arrayListOf()
        )
        vendorAdapter.notifyDataSetChanged()
        if (vendorList.size == 0){
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
         * @return A new instance of fragment VendorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VendorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun updateVendor(position: Int) {
        var dialog = Dialog(requireContext())
        var dialogBinding = DialogVendorBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (position > -1) {
            dialogBinding.name.setText(vendorList[position].tvVendorName)
            dialogBinding.phone.setText(vendorList[position].contact)
            dialogBinding.note.setText(vendorList[position].tvVendorNote)
            dialogBinding.category.setText(vendorList[position].vendorCategory)
            dialogBinding.email.setText(vendorList[position].tvEmail)
            dialogBinding.address.setText(vendorList[position].tvAddress)
            when(vendorList[position].status){
                1->dialogBinding.reserved.isChecked = true
                2->dialogBinding.pending.isChecked = true
            }
        }
        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, vendorArray)
        dialogBinding.vendorCategory.adapter = arrayAdapter
        dialogBinding.vendorCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedVendor = vendorArray[position]
                println("SelectedVendor: $selectedVendor")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        dialogBinding.addButton.setText(resources.getString(R.string.Update))
        dialog.show()
        dialogBinding.clickClose.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.addButton.setOnClickListener {
            if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                dialogBinding.name.error = resources.getString(R.string.enter_name)
            } else if (dialogBinding.phone.text.toString().isNullOrEmpty()) {
                dialogBinding.phone.error = resources.getString(R.string.enter_number)
            } else if (dialogBinding.rgVendor.checkedRadioButtonId == -1) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.select_status),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var status = if (dialogBinding.reserved.isChecked) {
                    1
                } else if (dialogBinding.pending.isChecked) {
                    2
                } else {
                    0
                }
                eventDataBase.vendorDao().update(
                    VendorDataClass(
                        eventId = eventId,
                        id = vendorList[position].id ?: 0,
                        tvVendorName = dialogBinding.name.text.toString(),
                        contact = dialogBinding.phone.text.toString(),
                        tvVendorNote = dialogBinding.note.text.toString(),
                        vendorCategory = selectedVendor,
                        tvEmail = dialogBinding.email.text.toString(),
                        tvAddress = dialogBinding.address.text.toString(),
                        status = status
                    )
                )
                getVendorListOf()
                dialog.dismiss()
            }
        }


    }

    override fun deleteVendor(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this))
        alertDialog.setPositiveButton("Yes") { _, _ ->
            // vendorList.removeAt(position)
            getVendorListOf()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        eventDataBase.vendorDao().delete(
            vendorList[position]
        )

        alertDialog.show()
    }

}
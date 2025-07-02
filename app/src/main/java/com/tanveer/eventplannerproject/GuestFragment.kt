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
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Guest.GuestAdapter
import com.tanveer.eventplannerproject.Guest.GuestDataClass
import com.tanveer.eventplannerproject.Guest.GuestInterface
import com.tanveer.eventplannerproject.databinding.DialogGuestBinding
import com.tanveer.eventplannerproject.databinding.FragmentGuestBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GuestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuestFragment : Fragment(), GuestInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentGuestBinding? = null
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var spinnerArray: ArrayAdapter<String>
    var groupArray = arrayOf("Unassigned Group", "Family", "Friend", "Colleagues", "Relatives")
    var menuArray =
        arrayOf("Unassigned Menu", "Adult Menu", "Children Menu", "Vegetarian Menu", "Diet Menu")
    var tableArray = arrayOf(
        "Unassigned Table",
        "Head Table",
        "First Row Table",
        "Second Row Table",
        "Third Row Table"
    )
    lateinit var linearLayoutManager: LinearLayoutManager
    var guestList = arrayListOf<GuestDataClass>()
    lateinit var guestAdapter: GuestAdapter
    lateinit var eventDataBase: EventDataBase
    private var TAG = "GuestFragment"
    var eventId = 0
    var selectedNAme = ""
    var selectedMenu = ""
    var selectedTable = ""


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
        binding = FragmentGuestBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guestAdapter = GuestAdapter(guestList, this)
        eventDataBase = EventDataBase.getInstance(requireContext())
        arguments?.let {
            eventId = it.getInt("eventId")

            Log.e(TAG, "onViewCreated:$eventId")

            getGuestListof()
        }


        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvGuestList?.layoutManager = linearLayoutManager
        binding?.rvGuestList?.adapter = guestAdapter
        binding?.btnFab?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = DialogGuestBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, groupArray)
            dialogBinding.spGroup.adapter = arrayAdapter
            dialogBinding.spGroup.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedNAme = groupArray[position]
                    println("SelectedNAme: $selectedNAme")

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, menuArray)
            dialogBinding.spMenu.adapter = arrayAdapter
            dialogBinding.spMenu.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedMenu = menuArray[position]
                    println("SelectedMenu: $selectedMenu")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tableArray)
            dialogBinding.spTable.adapter = arrayAdapter
            dialogBinding.spTable.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedTable = tableArray[position]
                    println("SelectedTable: $selectedTable")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            dialogBinding.closeButton.setOnClickListener {
                dialog.dismiss()
            }
//            dialogBinding.btnSave.setOnClickListener {
//                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
//                    dialogBinding.name.error = resources.getString(R.string.enter_name)
//                } else if (dialogBinding.phone.text.toString().isNullOrEmpty()) {
//                    dialogBinding.phone.error = resources.getString(R.string.Enter_PhoneNumber)
//                } else if (dialogBinding.email.text.toString().isNullOrEmpty()) {
//                    dialogBinding.email.error = resources.getString(R.string.enter_email)
//                } else if (dialogBinding.rgSend.checkedRadioButtonId == -1) {
//                    Toast.makeText(requireContext(), "select the status", Toast.LENGTH_SHORT).show()}
////                 else if {
////                    var send = if (dialogBinding.invitationSend.isChecked) {
////                        1
////                    } else if (dialogBinding.notSend.isChecked) {
////                        2
////                    } else {
////                        0
////                    }
//
//                else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
//                Toast.makeText(
//                    requireContext(),
//                    resources.getString(R.string.select_person),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//
////                var person = if (dialogBinding.adult.isChecked) {
////                    1
////                } else if (dialogBinding.child.isChecked) {
////                    2
////                } else if (dialogBinding.male.isChecked) {
////                    3
////                } else if (dialogBinding.female.isChecked) {
////                    4
////                } else {
////                    0
////                }
//
//                eventDataBase.guestDao().insertGuestDetail(
//                    GuestDataClass(
//                        eventId = eventId,
//                        GuestName = dialogBinding.name.text.toString(),
////                        person = person,
//                        Group = selectedNAme,
//                        Menu = selectedMenu,
//                        Table = selectedTable,
//                        Number = dialogBinding.phone.text.toString(),
//                        tvNote = dialogBinding.note.text.toString(),
//                        tvEmail = dialogBinding.email.text.toString(),
//                        tvAddress = dialogBinding.address.text.toString(),
////                        send = send
//
//                    )
//                )
//                Toast.makeText(requireContext(), "Guest details are added", Toast.LENGTH_SHORT)
//                getGuestListof()
//                dialog.dismiss()
//
//            }
//            }
//            dialog.show()
            dialogBinding.btnSave.setOnClickListener {
                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                    dialogBinding.name.error = resources.getString(R.string.enter_name)
                } else if (dialogBinding.phone.text.toString().isNullOrEmpty()) {
                    dialogBinding.phone.error = resources.getString(R.string.Enter_PhoneNumber)
                } else if (dialogBinding.email.text.toString().isNullOrEmpty()) {
                    dialogBinding.email.error = resources.getString(R.string.enter_email)
                } else if (dialogBinding.rgSend.checkedRadioButtonId == -1) {
                    Toast.makeText(requireContext(), "Select the invitation status", Toast.LENGTH_SHORT).show()
                } else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.select_person),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Get selected status from rgSend
                    val sendStatus = when (dialogBinding.rgSend.checkedRadioButtonId) {
                        R.id.invitationSend -> 1
                        R.id.notSend -> 2
                        else -> 0
                    }

                    // Get selected person type from radioGroup
                    val personType = when (dialogBinding.radioGroup.checkedRadioButtonId) {
                        R.id.adult -> 1
                        R.id.child -> 2
                        R.id.male -> 3
                        R.id.female -> 4
                        else -> 0
                    }

                    // Insert guest details into the database
                    eventDataBase.guestDao().insertGuestDetail(
                        GuestDataClass(
                            eventId = eventId,
                            GuestName = dialogBinding.name.text.toString(),
                            person = personType,
                            Group = selectedNAme,
                            Menu = selectedMenu,
                            Table = selectedTable,
                            Number = dialogBinding.phone.text.toString(),
                            tvNote = dialogBinding.note.text.toString(),
                            tvEmail = dialogBinding.email.text.toString(),
                            tvAddress = dialogBinding.address.text.toString(),
                            send = sendStatus
                        )
                    )
                    Toast.makeText(requireContext(), "Guest details are added", Toast.LENGTH_SHORT).show()
                    getGuestListof()
                    dialog.dismiss()
                }
            }
            dialog.show()


        }

    }

    private fun getGuestListof() {
        guestList.clear()
        guestList.addAll(
            eventDataBase.guestDao().getGuestListOf(eventId = eventId) ?: arrayListOf()
        )
        //  arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,guestList)
        //  binding
        guestAdapter.notifyDataSetChanged()
        if (guestList.size == 0) {
            binding?.emptyView?.visibility = View.VISIBLE
        } else {
            binding?.emptyView?.visibility = View.GONE
        }
    }


    override fun updateGuest(position: Int) {
        Dialog(requireContext()).apply {
            var dialogBinding = DialogGuestBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            dialogBinding.btnSave.setText(resources.getString(R.string.Update))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            show()
            if (position > -1) {
                dialogBinding.name.setText(guestList[position].GuestName)
                dialogBinding.phone.setText(guestList[position].Number)
                dialogBinding.group.setText(guestList[position].Group)
                dialogBinding.menu.setText(guestList[position].Menu)
                dialogBinding.table.setText(guestList[position].Table)
                dialogBinding.note.setText(guestList[position].tvNote)
                dialogBinding.email.setText(guestList[position].tvEmail)
                dialogBinding.address.setText(guestList[position].tvAddress)
                when (guestList[position].person) {
                    1 -> dialogBinding.adult.isChecked = true
                    2 -> dialogBinding.child.isChecked = true
                    3 -> dialogBinding.male.isChecked = true
                    4 -> dialogBinding.female.isChecked = true
                }
                when (guestList[position].send) {
                    1 -> dialogBinding.invitationSend.isChecked = true
                    2 -> dialogBinding.notSend.isChecked = true
                }
            }
            spinnerArray =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, groupArray)
            dialogBinding.spGroup.adapter = spinnerArray
            dialogBinding.spGroup.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedNAme = groupArray[position]
                    println("SelectedNAme: $selectedNAme")

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            spinnerArray =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, menuArray)
            dialogBinding.spMenu.adapter = spinnerArray
            dialogBinding.spMenu.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedMenu = menuArray[position]
                    println("SelectedMenu: $selectedMenu")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            spinnerArray =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tableArray)
            dialogBinding.spTable.adapter = spinnerArray
            dialogBinding.spTable.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedTable = tableArray[position]
                    println("SelectedTable: $selectedTable")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            dialogBinding.closeButton.setOnClickListener {
                dismiss()
            }
            dialogBinding.btnSave.setOnClickListener {
                if (dialogBinding.name.text.toString().isNullOrEmpty()) {
                    dialogBinding.name.error = resources.getString(R.string.enter_name)
                } else if (dialogBinding.phone.text.toString().isNullOrEmpty()) {
                    dialogBinding.phone.error = resources.getString(R.string.enter_number)
                } else if (dialogBinding.email.text.toString().isNullOrEmpty()) {
                    dialogBinding.email.error = resources.getString(R.string.enter_email)
                }
                else if (dialogBinding.rgSend.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        "Select the invitation status",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (dialogBinding.radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.select_person_type),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    val updatedSendStatus = when (dialogBinding.rgSend.checkedRadioButtonId) {
                R.id.invitationSend -> 1
                R.id.notSend -> 2
                else -> 0
            }

            val updatedPersonType = when (dialogBinding.radioGroup.checkedRadioButtonId) {
                R.id.adult -> 1
                R.id.child -> 2
                R.id.male -> 3
                R.id.female -> 4
                else -> 0
            }
                    eventDataBase.guestDao().updateGuestList(
                        GuestDataClass(

                            eventId = eventId,
                            id = guestList[position].id ?: 0,
                            GuestName = dialogBinding.name.text.toString(),
                            Number = dialogBinding.phone.text.toString(),
                            person = updatedPersonType,
                            Group = selectedNAme,
                            Menu = selectedMenu,
                            Table = selectedTable,
                            tvNote = dialogBinding.note.text.toString(),
                            tvEmail = dialogBinding.email.text.toString(),
                            tvAddress = dialogBinding.address.text.toString(),
                            send = updatedSendStatus
                        )
                    )

                    getGuestListof()
                    //                    guestAdapter.notifyDataSetChanged()
                    dismiss()
                }
            }

        }

    }

    override fun deleteGuest(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this))
        alertDialog.setPositiveButton("Yes") { _, _ ->
//            guestList.removeAt(position)
            eventDataBase.guestDao().deleteGuestList(
                guestList[position]
            )
            getGuestListof()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        alertDialog.show()
    }

}
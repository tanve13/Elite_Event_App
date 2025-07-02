package com.tanveer.eventplannerproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tanveer.eventplannerproject.Event.EventAdapter
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.Event.EventInterface
import com.tanveer.eventplannerproject.databinding.DialogDoneBinding
import com.tanveer.eventplannerproject.databinding.DialogEventDetailBinding
import com.tanveer.eventplannerproject.databinding.MainFragmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), EventInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: MainFragmentBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var eventDataBase: EventDataBase
     var eventDataClass= EventDataClass()
    var eventList = arrayListOf<EventDataClass>()
    lateinit var adapter: EventAdapter
    var dateFormat = SimpleDateFormat("dd/MMM/yyy")
    var timeFormat = SimpleDateFormat("hh:mm aa")
    var calendar = Calendar.getInstance()
    var timeCalendar = Calendar.getInstance()
    var fromateDate: String? = null
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
        binding = MainFragmentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventDataBase = EventDataBase.getInstance(requireContext())
        adapter = EventAdapter(requireContext(), eventList, this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvMainList?.layoutManager = linearLayoutManager
        binding?.rvMainList?.adapter = adapter
        getList()
        binding?.fabClick?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = DialogEventDetailBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.show()

            dialogBinding.etDate.setOnClickListener {
                var datePickerDialog = DatePickerDialog(
                    requireContext(),R.style.MyDatePicker, { _, year, month, date ->
                         calendar = Calendar.getInstance()
                        calendar.set(year, month, date)
                        fromateDate = dateFormat.format(calendar.time)
                        dialogBinding.etDate.setText(fromateDate)
                    }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePickerDialog.show()
            }
            dialogBinding.etTime.setOnClickListener {
                TimePickerDialog(
                    requireContext(),R.style.MyTimePicker, { _, hour, minute ->
                       timeCalendar = Calendar.getInstance()
                        timeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        timeCalendar.set(Calendar.MINUTE, minute)
                        dialogBinding.etTime.setText(timeFormat.format(timeCalendar.time))
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    false
                ).show()
            }
            dialogBinding.close.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btnADD.setOnClickListener {
                if (dialogBinding.etEnterName.text.toString().isNullOrEmpty()) {
                    dialogBinding.etEnterName.error =
                        resources.getString(R.string.enter_name)
                } else if (dialogBinding.etClientName.text.toString().isNullOrEmpty()) {
                    dialogBinding.etClientName.error =
                        resources.getString(R.string.enter_client_name)
                } else if (dialogBinding.etClientNumber.text.toString().isNullOrEmpty()) {
                    dialogBinding.etClientNumber.error =
                        resources.getString(R.string.enter_contact_no)
                } else if (dialogBinding.etClientNumber.text.toString().length < 10) {
                    dialogBinding.etClientNumber.error =
                        resources.getString(R.string.enter_valid_phone_number)
                } else if (dialogBinding.etClientAddress.text.toString().isNullOrEmpty()) {
                    dialogBinding.etClientAddress.error =
                        resources.getString(R.string.enter_address)
                } else if (dialogBinding.etDate.text.toString().isNullOrEmpty()) {
                    dialogBinding.etDate.error =
                        resources.getString(R.string.enter_date)
                } else if (dialogBinding.etTime.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTime.error =
                        resources.getString(R.string.enter_time)
                } else {
                    println("Date =${calendar.time} time = ${timeCalendar.time}")
                    eventDataBase.eventDao().insertEventDetail(
                        EventDataClass(
                            name = dialogBinding.etEnterName.text.toString(),
                            note = dialogBinding.etNote.text.toString(),
                            clientName = dialogBinding.etClientName.text.toString(),
                            clientPhoneNumber = dialogBinding.etClientNumber.text.toString(),
                            clientAddress = dialogBinding.etClientAddress.text.toString(),
                            dateTime=calendar.time,
                            time = timeCalendar.time
                        )
                    )

                    getList()
                    dialog.dismiss()
                }
                dialog.dismiss()
                showDoneDialog()
            }
        }
    }

    fun showDoneDialog() {
        var dialog = Dialog(requireContext()).apply {
            var dialogDone = DialogDoneBinding.inflate(layoutInflater)
            setContentView(dialogDone.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
            show()
            dialogDone.btnDone.setOnClickListener {
                getList()
                dismiss()
                Toast.makeText(
                    requireContext(),
                    "Event is Added",
                    Toast.LENGTH_SHORT
                )
            }
        }
        dialog.show()
    }

    private fun getList() {
        eventList.clear()
        eventList.addAll(eventDataBase.eventDao().getList() ?: arrayListOf())
        adapter.notifyDataSetChanged()
        if (eventList.size == 0){
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
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun updateEvent(position: Int) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogEventDetailBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialogBinding.btnADD.setText(resources.getString(R.string.Update))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog.show()
        if (position > -1) {
            dialogBinding.etEnterName.setText(eventList[position].name)
            dialogBinding.etClientName.setText(eventList[position].clientName)
            dialogBinding.etClientNumber.setText(eventList[position].clientPhoneNumber)
            dialogBinding.etClientAddress.setText(eventList[position].clientAddress)
//            dialogBinding.etDate.setText(dateFormat.format(eventDataClass.dateTime))
//            dialogBinding.etTime.setText(timeFormat.format(eventDataClass.time))
            dialogBinding.etDate.setText(
                   SimpleDateFormat("dd MMM yyyy").format(eventList[position].dateTime)
                       .toString()
               )
               dialogBinding.etTime.setText(
                    SimpleDateFormat("hh:mm aa").format(eventList[position].time).toString()
                   )
            dialogBinding.etNote.setText(eventList[position].note)
        }
        dialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.etDate.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(),R.style.MyDatePicker, { _, year, month, date ->
                    //Log.e(TAG, "year $year month $month date $date")
                    calendar = Calendar.getInstance()
                    calendar.set(year, month, date)
                   var fromateDate = dateFormat.format(calendar.time)
                    dialogBinding.etDate.setText(fromateDate)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }
        dialogBinding.etTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),R.style.MyTimePicker, { _, hour, minute ->
                    timeCalendar = Calendar.getInstance()
                    timeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    timeCalendar.set(Calendar.MINUTE, minute)
                    dialogBinding.etTime.setText(timeFormat.format(timeCalendar.time))
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
            ).show()
        }
        dialogBinding.btnADD.setOnClickListener {
            if (dialogBinding.etEnterName.text.toString().isNullOrEmpty()) {
                dialogBinding.etEnterName.error = resources.getString(R.string.Enter_name)
            } else {
                eventDataBase.eventDao().update(
                    EventDataClass(
                        id = eventList[position].id,
                        name = dialogBinding.etEnterName.text.toString(),
                        note = dialogBinding.etNote.text.toString(),
                        clientName = dialogBinding.etClientName.text.toString(),
                        clientPhoneNumber = dialogBinding.etClientNumber.text.toString(),
                        clientAddress = dialogBinding.etClientAddress.text.toString(),
                        dateTime=calendar.time,
                        time = timeCalendar.time

                    )
                )
                adapter.notifyDataSetChanged()
                getList()
                dialog.dismiss()

            }

        }
    }

            override fun deleteEvent(position: Int) {
                var alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this_event_))
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    eventList.removeAt(position)
                    getList()

                }
                alertDialog.setNegativeButton("no") { _, _ ->
                }
                alertDialog.setCancelable(false)
                eventDataBase.eventDao().delete(
                    eventList[position]
                )
                alertDialog.show()
            }

            override fun itemClick(position: Int) {
                var convertToString = Gson().toJson(eventList[position])
                findNavController().navigate(
                    R.id.singleEventDetailFragment,
                    bundleOf("event" to convertToString)
                )

            }

    override fun isCompleted(position: Int, isChecked: Boolean) {
        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(R.string.event_is_completed)
        alertDialog.setPositiveButton(R.string.yes) { _, _, ->
            eventList[position].isCompleted = true
            eventDataBase.eventDao().update(eventList[position])
            adapter.notifyItemChanged(position)
        }
        alertDialog.setNegativeButton(R.string.no){_,_ ->
            eventList[position].isCompleted = false
            eventDataBase.eventDao().update(eventList[position])
            adapter.notifyItemChanged(position)

        }
alertDialog.setCancelable(false)

        alertDialog.show()

    }
}

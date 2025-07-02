package com.tanveer.eventplannerproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.Schedule.ScheduleAdapter
import com.tanveer.eventplannerproject.Schedule.ScheduleDataClass
import com.tanveer.eventplannerproject.Schedule.ScheduleInterface
import com.tanveer.eventplannerproject.databinding.DialogScheduleBinding
import com.tanveer.eventplannerproject.databinding.FragmentScheduleBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment(), ScheduleInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentScheduleBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var scheduleList = arrayListOf<ScheduleDataClass>()
    lateinit var scheduleAdapter: ScheduleAdapter
    lateinit var eventDataClass: EventDataClass
    var eventDataBase: EventDataBase? = null
    private var TAG = "ScheduleFragment"
    var dateFormat = SimpleDateFormat("dd/MMM/yyy")
    var timeFormat = SimpleDateFormat("hh:mm aa")
    var calendar = Calendar.getInstance()
    var timeCalendar = Calendar.getInstance()
    var eventId = 0


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
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleAdapter = ScheduleAdapter(scheduleList, this)
        eventDataBase = EventDataBase.getInstance(requireContext())
        arguments?.let {
            eventId = it.getInt("eventId")

            Log.e(TAG, "onViewCreated:$eventId")

            getScheduleListOf()
        }

        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.rvSchedule?.layoutManager = linearLayoutManager
        binding?.rvSchedule?.adapter = scheduleAdapter
        binding?.fbAdd?.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogBinding = DialogScheduleBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.etDate.setOnClickListener {
                var datePickerDialog = DatePickerDialog(
                    requireContext(), { _, year, month, date ->
                        Log.e(TAG, "year $year month $month date $date")
                        var calendar = Calendar.getInstance()
                        calendar.set(year, month, date)
                        var formattedDate = dateFormat.format(calendar.time)
                        dialogBinding.etDate.setText(formattedDate)
                    }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePickerDialog.show()
            }
            dialogBinding.etTime.setOnClickListener {
                TimePickerDialog(
                    requireContext(), { _, hour, minute ->
//                    Log.e(TAG, "hour $hour minute $minute")
                        var calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        dialogBinding.etTime?.setText(timeFormat.format(calendar.time))
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    false
                ).show()

            }

            dialogBinding.closeClick.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etDate.text.toString().isNullOrEmpty()) {
                    dialogBinding.etDate.error = resources.getString(R.string.enter_date)
                } else if (dialogBinding.etTime.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTime.error = resources.getString(R.string.enter_time)
                } else {
                    eventDataBase?.scheduleDao()?.insertScheduleDetail(
                        ScheduleDataClass(
                            eventId = eventId,
                            note = dialogBinding.etScheduleNote.text.toString(),
                            createdDate = calendar.time,
                            createdTime = timeCalendar.time

                        )
                    )
                    Toast.makeText(
                        requireContext(),
                        "Schedule details are added",
                        Toast.LENGTH_SHORT
                    )
                    getScheduleListOf()
                    dialog.dismiss()

                }
            }
            dialog.show()
        }
    }


    private fun getScheduleListOf() {
        scheduleList.clear()
        scheduleList.addAll(
            eventDataBase?.scheduleDao()?.getScheduleListOf(eventId = eventId) ?: arrayListOf()
        )
        scheduleAdapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun updateSchedule(position: Int) {
        var calendar = Calendar.getInstance()

        var dialog = Dialog(requireContext())
        var dialogBinding = DialogScheduleBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.btnAdd.setText(resources.getString(R.string.Update))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        if (position > -1) {
            dialogBinding.etScheduleNote.setText(scheduleList[position].note)
            dialogBinding.etDate.setText(scheduleList[position].createdDate.toString())
            dialogBinding.etTime.setText(scheduleList[position].createdTime.toString())
            calendar.time = scheduleList[position].createdDate

        }
        dialogBinding.etDate.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, month, date ->
                    Log.e(TAG, "year $year month $month date $date")
                    calendar.set(year, month, date)
                    var formattedDate = dateFormat.format(calendar.time)
                    dialogBinding.etDate.setText(formattedDate)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }
        dialogBinding.etTime.setOnClickListener {
            TimePickerDialog(
                requireContext(), { _, hour, minute ->
//                    Log.e(TAG, "hour $hour minute $minute")
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    dialogBinding.etTime?.setText(timeFormat.format(calendar.time))
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
            ).show()

        }

        dialogBinding.closeClick.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnAdd.setOnClickListener {
            if (dialogBinding.etDate.text.toString().isNullOrEmpty()) {
                dialogBinding.etDate.error = resources.getString(R.string.enter_date)
            } else if (dialogBinding.etTime.text.toString().isNullOrEmpty()) {
                dialogBinding.etTime.error = resources.getString(R.string.enter_time)
            } else {
                eventDataBase?.scheduleDao()?.update(
                    ScheduleDataClass(
                        eventId = eventId,
                        id = scheduleList[position].id ?: 0,
                        note = dialogBinding.etScheduleNote.text.toString(),
                        createdDate = calendar.time,
                        createdTime = calendar.time

                    )
                )
                getScheduleListOf()
                // scheduleAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }


    }

    override fun deleteSchedule(position: Int) {
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(resources.getString(R.string.Do_you_want_to_delete_this))
        alertDialog.setPositiveButton("Yes") { _, _ ->
            // scheduleList.removeAt(position)
            getScheduleListOf()
        }
        alertDialog.setNegativeButton("no") { _, _ ->
        }
        eventDataBase?.scheduleDao()?.delete(
            scheduleList[position]
        )

        alertDialog.show()
    }
}
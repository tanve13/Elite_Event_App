package com.tanveer.eventplannerproject

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tanveer.eventplannerproject.Event.EventDaoInterface
import com.tanveer.eventplannerproject.Event.EventDataBase
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.databinding.FragmentHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentHistoryBinding? = null
    var dateFormat = SimpleDateFormat("dd/MMM/yyy")
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var eventList = arrayListOf<EventDataClass>()
    lateinit var dataBase: EventDataBase
    var startDate: String? = null
    var endDate: String? = null

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
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBase = EventDataBase.getInstance(requireContext())
        binding?.dateFrom?.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(), R.style.MyDatePicker, { _, year, month, date ->
                    startCalendar.set(year, month, date)
                    startDate = dateFormat.format(startCalendar.time)
                    startCalendar.set(year, month, date, 0, 0, 0)
                    binding?.dateFrom?.setText(startDate)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }
        binding?.toThis?.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                requireContext(), R.style.MyTimePicker, { _, year, month, date ->
                    endCalendar.set(year, month, date)
                    endDate = dateFormat.format(endCalendar.time)
                    endCalendar.set(year, month, date, 23, 59, 59)
                    binding?.toThis?.setText(endDate)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }

        binding?.generate?.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please select start and end dates",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        eventList.clear()
        eventList.addAll(
            dataBase?.eventDao()?.getTaskBetweenDates(
                startDate = startDate.toString(),
                endDate = endDate.toString()
            ) ?: arrayListOf()
        )
        showPieChart()
    }
    // actual pie chart
    private fun showPieChart() {
        val pieEntries = ArrayList<PieEntry>()
        var completedTask = 0
        var incompleteTask = 0
        val colorSet = arrayListOf(Color.rgb(243, 68, 68),
                                  Color.rgb(68, 103, 243))

        // Count completed and incomplete tasks
        for (task in eventList) {
            if (task.isCompleted == true) {
                completedTask++
            } else {
                incompleteTask++
            }
        }

        // Calculate completion percentage

        var completePercent = calculateCompletePercent(totalTask = eventList.size, completed = completedTask)
        pieEntries.add(PieEntry(completePercent.toFloat(), "Completed Event"))
        pieEntries.add(PieEntry((100 - completePercent).toFloat(), "Incomplete Event"))
            calculateCompletePercent(totalTask = eventList.size, completed = completedTask)
        val pieDataSet = PieDataSet(pieEntries, "\nComplete Incomplete Percent")
        pieDataSet.valueTextSize = 12f
        pieDataSet.setColors(colorSet)
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)


        binding?.pieChart?.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = true
            setUsePercentValues(true)
            legend.isEnabled = true
            invalidate() // Refresh chart
        }
    }

    private fun calculateCompletePercent(totalTask: Int, completed: Int): Double {
        return if (totalTask > 0) {
            (completed.toDouble() / totalTask.toDouble()) * 100
        } else {
            0.0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        dataBase  = EventDataBase.getInstance(requireContext())
//        binding?.dateFrom?.setOnClickListener {
//            var datePickerDialog = DatePickerDialog(
//                requireContext(), { _, year, month, date ->
//                    //Log.e(TAG, "year $year month $month date $date")
////                     startCalendar = Calendar.getInstance()
//                    startCalendar.set(year, month, date)
//                    startDate = dateFormat.format(startCalendar.time)
//                    startCalendar.set(year, month, date, 0, 0, 0)
//                   binding?.dateFrom?.setText(startDate)
//                }, Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DATE)
//            )
//            datePickerDialog.show()
//        }
//        binding?.toThis?.setOnClickListener {
//            var datePickerDialog = DatePickerDialog(
//                requireContext(), { _, year, month, date ->
//                    //Log.e(TAG, "year $year month $month date $date")
////                     endCalendar = Calendar.getInstance()
//                    endCalendar.set(year, month, date)
//                    endDate = dateFormat.format(endCalendar.time)
//                    endCalendar.set(year, month, date, 23, 59, 59)
//                    binding?.toThis?.setText(endDate)
//                }, Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DATE)
//            )
//            datePickerDialog.show()
//        }
//        binding?.generate?.setOnClickListener{
//            getData()
//        }
//    }
//    fun getData(){
//        taskList.clear()
//        taskList.addAll(dataBase?.eventDao()?.getTaskBetweenDates(startDate = startDate.toString(), endDate = endDate.toString()) ?: arrayListOf())
//        showPieChart()
//    }
//    private fun showPieChart() {
//        val pieEntries = ArrayList<PieEntry>()
//        var completedTask = 0
//        var incompleteTask = 0
//        val colorSet = java.util.ArrayList<Int>()
//        colorSet.add(Color.rgb(243,68,68))
//        colorSet.add(Color.rgb(68,103,243))
//
//        for(tasks in taskList){
//            if(tasks.isCompleted == true){
//                completedTask++
//            }else{
//                incompleteTask++
//            }
//        }
//
//        var completePercent = calculateCompletePercent(totalTask = taskList.size, completed = completedTask)
//        pieEntries.add(PieEntry(completePercent.toFloat(), "Completed Event"))
//        pieEntries.add(PieEntry((100 - completePercent).toFloat(), "Incomplete Event"))
//
//        val pieDataSet = PieDataSet(pieEntries, "\nComplete Incomplete Percent")
//        pieDataSet.valueTextSize = 12f
//        pieDataSet.setColors(colorSet)
//        val pieData = PieData(pieDataSet)
//        pieData.setDrawValues(true)
//
//        binding?.pieChart?.setData(pieData)
//        binding?.pieChart?.invalidate()
//    }
//    private fun calculateCompletePercent(totalTask: Int, completed : Int): Double {
//        var percent = 0.0
//        percent =  (completed.toDouble() / totalTask.toDouble()) * 100
//        return percent
//    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HistoryFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HistoryFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}
//import android.app.DatePickerDialog
//import android.graphics.Color
//import android.os.Bundle
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.github.mikephil.charting.data.PieData
//import com.github.mikephil.charting.data.PieDataSet
//import com.github.mikephil.charting.data.PieEntry
//import com.tanveer.eventplannerproject.Event.EventDataBase
//import com.tanveer.eventplannerproject.Event.EventDataClass
//import com.tanveer.eventplannerproject.databinding.FragmentHistoryBinding
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//
//class HistoryFragment : Fragment() {
//    private var param1: String? = null
//    private var param2: String? = null
//    private var binding: FragmentHistoryBinding? = null
//    private val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
//    private val startCalendar = Calendar.getInstance()
//    private val endCalendar = Calendar.getInstance()
//    private var taskList = arrayListOf<EventDataClass>()
//    private lateinit var dataBase: EventDataBase
//    private var startDate: String? = null
//    private var endDate: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentHistoryBinding.inflate(layoutInflater)
//        return binding?.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        dataBase = EventDataBase.getInstance(requireContext())
//
//        binding?.dateFrom?.setOnClickListener {
//            DatePickerDialog(
//                requireContext(), { _, year, month, dayOfMonth ->
//                    startCalendar.set(year, month, dayOfMonth)
//                    startDate = dateFormat.format(startCalendar.time)
//                    startCalendar.set(year, month, dayOfMonth, 0, 0, 0)
//                    binding?.dateFrom?.text = startDate
//                }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//
//        binding?.toThis?.setOnClickListener {
//            DatePickerDialog(
//                requireContext(), { _, year, month, dayOfMonth ->
//                    endCalendar.set(year, month, dayOfMonth)
//                    endDate = dateFormat.format(endCalendar.time)
//                    endCalendar.set(year, month, dayOfMonth, 23, 59, 59)
//                    binding?.toThis?.text = endDate
//                }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//
//        binding?.generate?.setOnClickListener {
//            getData()
//        }
//    }
//
//    private fun getData() {
//        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
//            Toast.makeText(requireContext(), "Please select start and end dates", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        taskList.clear()
//        taskList.addAll(dataBase.eventDao().getTaskBetweenDates(startDate = startDate!!, endDate = endDate!!))
//        showPieChart()
//    }
//
//    private fun showPieChart() {
//        val pieEntries = ArrayList<PieEntry>()
//        var completedTask = 0
//        var incompleteTask = 0
//        val colorSet = arrayListOf(Color.rgb(243, 68, 68), Color.rgb(68, 103, 243))
//
//        for (task in taskList) {
//            if (task.isCompleted== true) {
//                completedTask++
//            } else {
//                incompleteTask++
//            }
//        }
//
//        val completePercent = calculateCompletePercent(taskList.size, completedTask)
//        pieEntries.add(PieEntry(completePercent.toFloat(), "Completed Event"))
//        pieEntries.add(PieEntry((100 - completePercent).toFloat(), "Incomplete Event"))
//
//        val pieDataSet = PieDataSet(pieEntries, "Event Completion")
//        pieDataSet.valueTextSize = 12f
//        pieDataSet.colors = colorSet
//
//        val pieData = PieData(pieDataSet)
//        pieData.setDrawValues(true)
//
//        binding?.pieChart?.apply {
//            data = pieData
//            description.isEnabled = false
//            isRotationEnabled = false
//            setUsePercentValues(true)
//            legend.isEnabled = true
//            invalidate()
//        }
//    }
//
//    private fun calculateCompletePercent(totalTask: Int, completed: Int): Double {
//        return if (totalTask > 0) {
//            (completed.toDouble() / totalTask.toDouble()) * 100
//        } else {
//            0.0
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HistoryFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

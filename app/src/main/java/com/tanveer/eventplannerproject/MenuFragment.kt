package com.tanveer.eventplannerproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.tanveer.eventplannerproject.databinding.DialogDatetimeBinding
import com.tanveer.eventplannerproject.databinding.FragmentMenuBinding
import com.tanveer.eventplannerproject.databinding.MainFragmentBinding
import com.tanveer.eventplannerproject.databinding.ThemeViewBinding
import com.tanveer.eventplannerproject.databinding.TimeViewBinding
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentMenuBinding? = null
    var array = arrayListOf<String>()
    var timeArray = arrayListOf<String>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mainActivity=activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(resources.getString(R.string.elite_events),
            MODE_PRIVATE)
        editor = sharedPreferences.edit()
        array.addAll(resources.getStringArray(R.array.date_time_format))
        timeArray.addAll(resources.getStringArray(R.array.time_format))
        binding?.tvFeedback?.setOnClickListener {
            try{
                var intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/email")
                startActivity(intent)
            } catch(exception : Exception){
                Toast.makeText(requireContext(), "sorry cannot open email", Toast.LENGTH_SHORT).show()}
        }
        binding?.tvShare?.setOnClickListener {
            try{
                var intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/email")
                startActivity(intent)
            } catch(exception : Exception){
                Toast.makeText(requireContext(), "sorry cannot open email", Toast.LENGTH_SHORT).show()}
        }
        binding?.lvDate?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = DialogDatetimeBinding.inflate(layoutInflater)
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.listView.setOnItemClickListener { parent, view, position, id ->
                    Log.e("TAG", "dialog.listView.selectedItem ${dialog.listView.selectedItem}")
                    saveDateTimeFormat(array[position])
                    Log.e("TAG", "dialog.listView.selectedItem ${array[position]}")
                    dismiss()
                }
            }
        }
        binding?.theme?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(resources.getString(R.string.change_theme))
                setMessage(resources.getString(R.string.change_theme_msg))
                setPositiveButton(resources.getString(R.string.dark)) { _, _ ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    val editor = requireContext().getSharedPreferences(
                        resources.getString(R.string.app_name),
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putBoolean("dark", true)
                    editor.apply()
                    requireActivity().recreate()
                }
                setNegativeButton(resources.getString(R.string.light)) { _, _ ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    val editor = requireContext().getSharedPreferences(
                        resources.getString(R.string.app_name),
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putBoolean("dark", false)
                    editor.apply()
                    requireActivity().recreate()
                }
            }.show()
        }
        binding?.lvTime?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = TimeViewBinding.inflate(layoutInflater)
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.listView.setOnItemClickListener{ parent, view, position, id->
                    saveTime(timeArray[position])
                    dismiss()
                }
            }
        }
    }
    private fun saveDateTimeFormat(dateTimeFormat: String){
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            resources.getString(R.string.app_name),MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("dateTimeFormat",dateTimeFormat)
        editor.apply()
    }
    private fun saveTheme(isEnabled: Boolean){
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            resources.getString(R.string.app_name),MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("themeFormat",isEnabled)
        editor.apply()
    }
    private fun saveTime(timeFormat: String){
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            resources.getString(R.string.app_name),MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("timeFormat",timeFormat)
        editor.apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
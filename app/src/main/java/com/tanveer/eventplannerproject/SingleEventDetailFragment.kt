package com.tanveer.eventplannerproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.tanveer.eventplannerproject.Event.EventDataClass
import com.tanveer.eventplannerproject.databinding.FragmentSingleEventDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleEventDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleEventDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentSingleEventDetailBinding? = null
    var eventDataClass = EventDataClass()
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
        binding = FragmentSingleEventDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            var event = it.getString("event")
            eventDataClass = Gson().fromJson(event, EventDataClass::class.java)
            binding?.clientName?.setText(eventDataClass.clientName)
            binding?.clientNumber?.setText(eventDataClass.clientPhoneNumber)
        }
        binding?.guest?.setOnClickListener {
            findNavController().navigate(R.id.action_singleEventDetailFragment_to_guestFragment,
                bundleOf("eventId" to eventDataClass.id))
        }
        binding?.budget?.setOnClickListener {
            findNavController().navigate(R.id.action_singleEventDetailFragment_to_budgetFragment,
                bundleOf("eventId" to eventDataClass.id))
        }
        binding?.vendor?.setOnClickListener {
            findNavController().navigate(R.id.action_singleEventDetailFragment_to_vendorFragment,
                bundleOf("eventId" to eventDataClass.id))
        }
//        binding?.schedule?.setOnClickListener {
//            findNavController().navigate(R.id.action_singleEventDetailFragment_to_scheduleFragment,
//                bundleOf("eventId" to eventDataClass.id))
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingleEventDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SingleEventDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
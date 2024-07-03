package com.jamycake.test_task.maps.presentation.location_disabled

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.jamycake.test_task.google_maps.R
import com.jamycake.test_task.google_maps.databinding.FragmentLocationDisabledBinding
import com.jamycake.test_task.maps.presentation.isLocationEnabled

class LocationDisabledFragment : DialogFragment(R.layout.fragment_location_disabled) {

    private var binding: FragmentLocationDisabledBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationDisabledBinding.bind(view)
        binding?.apply {
            enable.setOnClickListener {
                val intent =  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLocationEnabled(requireContext())){
            findNavController().navigateUp()
            setFragmentResult("enabled", Bundle.EMPTY)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
package com.jamycake.test_task.maps.presentation.point_options

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jamycake.test_task.google_maps.R
import com.jamycake.test_task.google_maps.databinding.FragmentPointOptionsBinding

class PointOptionsFragment : DialogFragment(R.layout.fragment_point_options) {

    private var binding: FragmentPointOptionsBinding? = null
    private val viewModel: PointOptionsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPointOptionsBinding.bind(view)
        viewModel.id = requireArguments().getString("id")?:""

        binding?.apply {
            delete.setOnClickListener {
                viewModel.delete()
                findNavController().navigateUp()
            }
            buildRoute.setOnClickListener {
                setFragmentResult("route", Bundle().apply {
                    putString("id", viewModel.id)
                })
                findNavController().navigateUp()
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
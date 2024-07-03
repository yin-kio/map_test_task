package com.jamycake.test_task.maps.presentation.name_input

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jamycake.test_task.google_maps.R
import com.jamycake.test_task.google_maps.databinding.FragmentNameInputBinding

class NameInputFragment : DialogFragment(R.layout.fragment_name_input) {


    private var binding: FragmentNameInputBinding? = null
    private val viewModel: NameInputViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNameInputBinding.bind(view)
        viewModel.id = requireArguments().getString("id")?:""

        binding?.apply {
            nameInput.setText(viewModel.name)
            nameInput.doOnTextChanged { text, _, _, _ ->
                viewModel.name = text?.toString()?:""
            }
            ok.setOnClickListener {
                viewModel.updateName()
                findNavController().navigateUp()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}
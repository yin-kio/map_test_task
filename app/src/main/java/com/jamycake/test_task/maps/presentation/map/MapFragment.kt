package com.jamycake.test_task.maps.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.jamycake.test_task.google_maps.R
import com.jamycake.test_task.google_maps.databinding.FragmentMapsBinding
import com.jamycake.test_task.maps.presentation.isLocationEnabled
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.launch


class MapFragment : Fragment(R.layout.fragment_maps) {

    private val viewModel: MapViewModel by viewModels()

    private var _binding: FragmentMapsBinding? = null
    private val binding get() =  _binding!!

    private var map: MapWrapper? = null


    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it){
            requestPermission()
        } else {
            updateCurrentLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapKitFactory.initialize(requireContext().applicationContext)

        _binding = FragmentMapsBinding.bind(view)
        map = MapWrapper(context = requireContext(),
            map = { binding.mapview.map },
            onPointClick = { findNavController().navigate(R.id.toPointOptions, Bundle().apply {
                putString("id", it)
            }) },
            onLongClick = viewModel::addPoint
        )


        if (hasPermission()){
            updateCurrentLocation()
        } else {
            requestPermission()
        }


        setFragmentResultListener("route"){requestKey, bundle ->
            val id = bundle.getString("id")?:""
            viewModel.state.value.savedPoints.find { it.id == id}?.let {
                map?.buildRoute(it)
            }
        }

        setFragmentResultListener("enabled"){ _,_ ->
            updateCurrentLocation()
        }


    }


    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.getCurrentLocation(
            CurrentLocationRequest.Builder()
                .build(),
            null
        ).addOnSuccessListener {location ->
            location?.let { map?.setCurrentLocation(it) }

        }

    }



    private fun requestPermission(){
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun hasPermission() : Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }


    override fun onResume() {
        super.onResume()
        val navController = findNavController()
        if (!isLocationEnabled(requireContext()) && navController.currentDestination?.id == R.id.maps){
            findNavController().navigate(R.id.toLocationDisabled)
        }
    }

    override fun onStart() {
        super.onStart()
        _binding?.mapview?.onStart()
        MapKitFactory.getInstance().onStart()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.id.collect{
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.maps){
                    navController.navigate(R.id.toNameInput, Bundle().apply {
                        putString("id", it)
                    })
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect{
                map?.setPoints(it.savedPoints)
            }
        }

    }



    override fun onStop() {
        _binding?.mapview?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map = null
    }


}
package com.jamycake.test_task.maps.presentation

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings

fun isLocationEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isLocationEnabled
        } else {
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            return (mode != Settings.Secure.LOCATION_MODE_OFF)
        }
    }
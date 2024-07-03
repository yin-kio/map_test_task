package com.jamycake.test_task.maps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey("80d3891c-d088-40d5-b42b-c1de1d790575")
    }

}
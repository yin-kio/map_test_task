package com.jamycake.test_task.maps.presentation.map

import android.content.Context
import android.location.Location
import androidx.annotation.DrawableRes
import com.jamycake.test_task.google_maps.R
import com.jamycake.test_task.maps.domain.DomainPoint
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

class MapWrapper(
    private val context: Context,
    private val map: () -> Map,
    private val onPointClick: (id: String) -> Unit,
    private val onLongClick: (latitude: Double, longitude: Double) -> String
) {

    private var currentLocation: Point? = null
    private var polyline: PolylineMapObject? = null
    private val inputListener: InputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {

        }

        override fun onMapLongTap(map: Map, point: Point) {
            val id =  onLongClick(point.latitude, point.longitude)
            addRedMark(point, id = id, name = "")

        }
    }

    private val mapObjectTapListener = MapObjectTapListener { mapObject, _ ->
        onPointClick(mapObject.userData as String)
        true
    }


    private val router by lazy { DirectionsFactory.getInstance().createDrivingRouter(
        DrivingRouterType.ONLINE
    ) }

    init {
        map().addInputListener(inputListener)
    }

    fun setCurrentLocation(location: Location){
        val point =  Point(
            location.latitude,
            location.longitude
        )

        currentLocation = point

        setCurrentLocation(point)
    }

    private fun setCurrentLocation(point: Point) {
        map().move(
            CameraPosition(
                point,
                /* zoom = */ 17.0f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 0.0f
            ),
        )

        addPlaceMark(point, R.drawable.location_2955, id = "", name = "")
    }


    fun setPoints(pointsInfos: List<DomainPoint>){
        map().mapObjects.clear()
        currentLocation?.let { setCurrentLocation(it) }
        pointsInfos.forEach { addPoint(it) }
    }

    private fun addPoint(domainPoint: DomainPoint){
        val point = yandexPoint(domainPoint)
        addRedMark(point, domainPoint.id, name = domainPoint.name)
    }

    private fun addRedMark(point: Point, id: String, name: String) {
        map().move(
            CameraPosition(
                point,
                17f,
                0f,
                0f
            )
        )
        addPlaceMark(
            point, R.drawable.drop_pin_10077,
            id = id,
            name = name,
            tapListener = mapObjectTapListener)
    }

    fun buildRoute(domainPoint: DomainPoint) {
        val point = yandexPoint(domainPoint)
        val drivingOptions = DrivingOptions().apply {
            routesCount = 1
        }
        val points = buildList {
            add(RequestPoint(currentLocation!!, RequestPointType.WAYPOINT, "", null))
            add(RequestPoint(point, RequestPointType.WAYPOINT, "", null))
        }

        router.requestRoutes(points,
            drivingOptions,
            VehicleOptions(),
            object : DrivingSession.DrivingRouteListener {
                override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {

                    drivingRoutes.forEach {
                        if (polyline?.isValid == true){
                            polyline?.let { map().mapObjects.remove(it) }
                        }

                        polyline = map().mapObjects.addPolyline(it.geometry)

                    }
                }

                override fun onDrivingRoutesError(p0: Error) {

                }


            }

        )
    }

    private fun addPlaceMark(
        point: Point,
        @DrawableRes
        resId: Int,
        id: String,
        name: String,
        tapListener: MapObjectTapListener? = null
    ) {
        val imageProvider = ImageProvider.fromResource(context, resId)
        val placemark = map().mapObjects.addPlacemark()
        placemark.geometry = point
        placemark.setIcon(imageProvider)
        placemark.userData = id
        placemark.setText(name)
        tapListener?.let { placemark.addTapListener(tapListener) }



    }

    private fun yandexPoint(domainPoint: DomainPoint): Point {
        val point = Point(
            domainPoint.latitude,
            domainPoint.longitude
        )
        return point
    }



}


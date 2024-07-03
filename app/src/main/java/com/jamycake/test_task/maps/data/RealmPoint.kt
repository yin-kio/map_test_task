package com.jamycake.test_task.maps.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmPoint() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}
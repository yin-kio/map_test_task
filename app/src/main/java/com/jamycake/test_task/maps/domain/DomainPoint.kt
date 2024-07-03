package com.jamycake.test_task.maps.domain

import java.util.UUID

data class DomainPoint(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val latitude: Double,
    val longitude: Double
)
package com.jamycake.test_task.maps.presentation.map

import com.jamycake.test_task.maps.domain.DomainPoint

data class MapState(
    val savedPoints: List<DomainPoint> = emptyList()
)
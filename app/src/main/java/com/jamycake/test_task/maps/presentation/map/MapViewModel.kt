package com.jamycake.test_task.maps.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamycake.test_task.maps.data.RealmRepo
import com.jamycake.test_task.maps.domain.DomainPoint
import com.jamycake.test_task.maps.domain.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {


    private val repo: Repo = RealmRepo.instance()


    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _id = MutableSharedFlow<String>()
    val id = _id.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            repo.observablePoints().collect{ points ->
                _state.update { it.copy(savedPoints = points) }
            }
        }
    }



    fun addPoint(latitude: Double, longitude: Double) : String{
        val point = DomainPoint(
            latitude = latitude,
            longitude = longitude
        )
        viewModelScope.launch(Dispatchers.IO) {

            repo.save(point)
            _id.emit(point.id)

        }
        return point.id
    }


}
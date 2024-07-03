package com.jamycake.test_task.maps.presentation.point_options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamycake.test_task.maps.data.RealmRepo
import com.jamycake.test_task.maps.domain.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PointOptionsViewModel : ViewModel() {

    private val repo: Repo = RealmRepo.instance()

    var id: String = ""


    fun delete(){
        if (id.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(id)
        }
    }

}
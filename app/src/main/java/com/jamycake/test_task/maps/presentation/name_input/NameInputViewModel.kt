package com.jamycake.test_task.maps.presentation.name_input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamycake.test_task.maps.data.RealmRepo
import com.jamycake.test_task.maps.domain.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NameInputViewModel : ViewModel() {

    private val repo: Repo = RealmRepo.instance()

    var name: String = ""
    var id: String = ""

    fun updateName(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateName(id, name)
        }
    }


}
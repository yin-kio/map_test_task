package com.jamycake.test_task.maps.domain

import kotlinx.coroutines.flow.Flow

interface Repo {

    suspend fun save(domainPoint: DomainPoint)
    suspend fun updateName(id: String, name: String)
    suspend fun delete(id: String)

    fun observablePoints() : Flow<List<DomainPoint>>


}
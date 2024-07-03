package com.jamycake.test_task.maps.data

import com.jamycake.test_task.maps.domain.DomainPoint
import com.jamycake.test_task.maps.domain.Repo
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealmRepo private constructor(
    private val realm: Realm
) : Repo{


    override suspend fun save(domainPoint: DomainPoint){
        realm.write {
            copyToRealm(realmPoint(domainPoint),
                updatePolicy = UpdatePolicy.ALL)
        }
    }

    private fun realmPoint(domainPoint: DomainPoint) : RealmPoint {
        return RealmPoint().apply {
            id = domainPoint.id
            name = domainPoint.name
            latitude = domainPoint.latitude
            longitude = domainPoint.longitude
        }
    }


    override fun observablePoints() : Flow<List<DomainPoint>>{
         return realm.query<RealmPoint>().find().asFlow()
             .map { it.list.map {
                 DomainPoint(
                     id = it.id,
                     name = it.name,
                     latitude = it.latitude,
                     longitude = it.longitude
                 )
             } }
    }

    override suspend fun updateName(
        id: String,
        name: String
    ){
        val point = realm.query<RealmPoint>("id == '$id'").first().find()
        point?.let {
            realm.write { copyToRealm(RealmPoint().apply {
                this.id = id
                this.name = name
                longitude = point.longitude
                latitude = point.latitude
            }, updatePolicy = UpdatePolicy.ALL) }
        }
    }

    override suspend fun delete(id: String){
        val point = realm.query<RealmPoint>("id == '$id'").find().firstOrNull()
        point?:return
        realm.write {
            findLatest(point)?.also { delete(it) }
        }

    }

    companion object {
        private var repo: RealmRepo? = null

        fun instance() : RealmRepo {
            if (repo == null){

                val config = RealmConfiguration
                    .create(schema = setOf(RealmPoint::class))

                repo = RealmRepo(realm = Realm.open(config))
            }
            return repo!!
        }
    }

}
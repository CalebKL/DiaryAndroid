package com.example.diaryandroid.data

import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Constants.APP_ID
import com.example.diaryandroid.util.Resource
import com.example.diaryandroid.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId

object MongoDB: MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user!= null){
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions {sub->
                    add(
                        query = sub.query("ownerId == $0", user.identity),
                        name = "Diaries"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
       return if (user != null){
            try {
                realm.query<Diary>(query = "ownerId ==$0", user.identity)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow().map {result->
                        Resource.Success(
                            data = result.list.groupBy {
                                it.date.toInstant().atZone(
                                    ZoneId.systemDefault()
                                ).toLocalDate()
                            }
                        )
                    }
            }catch (e:Exception){
                flow {
                    emit(Resource.Error(message = e.localizedMessage?:"Exception thrown: Invalid"))

                }
            }
        }else{
            flow {
                emit(Resource.Error(message = "User is not Logged in"))
            }
        }
    }
}

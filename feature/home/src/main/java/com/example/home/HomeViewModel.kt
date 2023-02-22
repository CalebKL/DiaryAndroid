package com.example.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.util.connectivity.ConnectivityObserver
import com.example.util.connectivity.NetworkConnectivityObserver
import com.example.mongo.database.ImagesToDeleteDao
import com.example.mongo.database.entity.ImageToDelete
import com.example.mongo.repository.Diaries
import com.example.mongo.repository.MongoDB
import com.example.util.model.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imagesToDeleteDao: ImagesToDeleteDao
):ViewModel() {
    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var diaries: MutableState<Diaries> = mutableStateOf(Resource.Idle)
    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getDiaries()
        viewModelScope.launch{
            connectivity.observer().collect{
                network = it
            }
        }
    }
    fun getDiaries(zonedDateTime: ZonedDateTime? = null){
        dateIsSelected = zonedDateTime !=null
        diaries.value = Resource.Loading
        if (dateIsSelected && zonedDateTime != null){
            observeFilteredDiaries(zonedDateTime = zonedDateTime)
        }else{
            observeAllDiaries()
        }
    }
    private fun observeAllDiaries(){
      allDiariesJob=  viewModelScope.launch {
          if (::filteredDiariesJob.isInitialized){
              filteredDiariesJob.cancelAndJoin()
          }
           MongoDB.getAllDiaries().collect{ result->
               diaries.value = result
           }
       }
    }
    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime){
        filteredDiariesJob = viewModelScope.launch {
            if (::allDiariesJob.isInitialized){
                allDiariesJob.cancelAndJoin()
            }
            MongoDB.getFilteredDiaries(zonedDateTime = zonedDateTime).collect{ result->
                diaries.value = result
            }
        }
    }
    fun deleteAllDiaries(
        onSuccess:()->Unit,
        onError:(Throwable)->Unit
    ){
        if (network == ConnectivityObserver.Status.Available){
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/${userId}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach {ref->
                        val imagePath = "images/${userId}/${ref.name}"
                        storage.child(imagePath).delete()
                            .addOnFailureListener{
                               viewModelScope.launch (Dispatchers.IO){
                                   imagesToDeleteDao.addImageToDelete(
                                       ImageToDelete(
                                           remoteImagePath = imagePath
                                       )
                                   )
                               }
                            }
                    }
                    viewModelScope.launch(Dispatchers.IO){
                        val result = MongoDB.deleteAllDiaries()
                        if (result is Resource.Success){
                            withContext(Dispatchers.Main){
                                onSuccess
                            }
                        }else if (result is Resource.Error){
                            withContext(Dispatchers.Main){
                                onError(result.error)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    onError(it)
                }
        }else{
            onError(Exception("No Internet Connection"))
        }
    }
}
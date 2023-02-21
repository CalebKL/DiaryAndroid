package com.example.diaryandroid.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryandroid.connectivity.ConnectivityObserver
import com.example.diaryandroid.connectivity.NetworkConnectivityObserver
import com.example.diaryandroid.data.Diaries
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.data.database.ImagesToDeleteDao
import com.example.diaryandroid.data.database.entity.ImageToDelete
import com.example.diaryandroid.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imagesToDeleteDao: ImagesToDeleteDao
):ViewModel() {

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var diaries: MutableState<Diaries> = mutableStateOf(Resource.Idle)

    init {
        observeAllDiaries()
        viewModelScope.launch{
            connectivity.observer().collect{
                network = it
            }
        }
    }
    private fun observeAllDiaries(){
       viewModelScope.launch {
           MongoDB.getAllDiaries().collect{result->
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
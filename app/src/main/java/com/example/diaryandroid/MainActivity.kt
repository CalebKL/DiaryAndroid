package com.example.diaryandroid

import android.os.Bundle
import android.provider.UserDictionary
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.diaryandroid.navigation.SetupNavGraph
import com.example.ui.theme.DiaryAndroidTheme
import com.example.mongo.database.ImageToUploadDao
import com.example.mongo.database.ImagesToDeleteDao
import com.example.mongo.database.entity.ImageToDelete
import com.example.mongo.database.entity.ImageToUpload
import com.example.ui.R
import com.example.util.LottieLoader
import com.example.util.Screen
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao

    @Inject
    lateinit var imagesToDeleteDao: ImagesToDeleteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAndroidTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = Screen.Splash.route, navController =navController)
            }
        }
        cleanupCheck(
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
            imagesToDeleteDao = imagesToDeleteDao
        )
    }
}
private fun cleanupCheck(
    scope:CoroutineScope,
    imageToUploadDao: ImageToUploadDao,
    imagesToDeleteDao: ImagesToDeleteDao
){
    scope.launch(Dispatchers.IO) {
        val result = imageToUploadDao.getAllImages()
        result.forEach {imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload = imageToUpload,
                onSuccess = {
                    scope.launch (Dispatchers.IO){
                        imageToUploadDao.cleanupImage(imageToUpload.id)

                    }
                }
            )
        }
        val result2 = imagesToDeleteDao.getAllImages()
        result2.forEach {imageToDelete ->
            retryDeletingImageFromFirebase(
                imageToDelete = imageToDelete,
                onSuccess = {
                    scope.launch (Dispatchers.IO){
                        imagesToDeleteDao.cleanupImage(imageId = imageToDelete.id)

                    }
                }
            )
        }
    }
}
fun retryDeletingImageFromFirebase(
    imageToDelete: ImageToDelete,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete()
        .addOnSuccessListener { onSuccess() }
}

fun retryUploadingImageToFirebase(
    imageToUpload: ImageToUpload,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath).putFile(
        imageToUpload.imageUri.toUri(),
        storageMetadata { },
        imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}

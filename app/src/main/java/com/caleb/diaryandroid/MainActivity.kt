package com.caleb.diaryandroid

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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.caleb.diaryandroid.navigation.SetupNavGraph
import com.caleb.ui.theme.DiaryAndroidTheme
import com.caleb.mongo.database.ImageToUploadDao
import com.caleb.mongo.database.ImagesToDeleteDao
import com.caleb.mongo.database.entity.ImageToDelete
import com.caleb.mongo.database.entity.ImageToUpload
import com.caleb.ui.R
import com.caleb.util.Constants.APP_ID
import com.caleb.util.LottieLoader
import com.caleb.util.Screen
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
    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAndroidTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }
        cleanupCheck(
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
            imagesToDeleteDao = imagesToDeleteDao
        )
    }
}
private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
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

package com.example.diaryandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.diaryandroid.data.database.ImageToUploadDao
import com.example.diaryandroid.navigation.Screen
import com.example.diaryandroid.navigation.SetupNavGraph
import com.example.diaryandroid.presentation.ui.theme.DiaryAndroidTheme
import com.example.diaryandroid.util.retryUploadingImageToFirebase
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao
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
        cleanupCheck(scope = lifecycleScope, imageToUploadDao = imageToUploadDao)
    }
}
private fun cleanupCheck(
    scope:CoroutineScope,
    imageToUploadDao: ImageToUploadDao
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
    }
}
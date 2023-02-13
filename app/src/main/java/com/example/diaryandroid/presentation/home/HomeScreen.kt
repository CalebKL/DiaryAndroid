package com.example.diaryandroid.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.diaryandroid.R
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.presentation.destinations.AuthenticationScreenDestination
import com.example.diaryandroid.presentation.home.components.DisplayAlertDialog
import com.example.diaryandroid.presentation.home.components.HomeContent
import com.example.diaryandroid.presentation.home.components.HomeTopBar
import com.example.diaryandroid.presentation.home.components.NavigationDrawer
import com.example.diaryandroid.util.Constants.APP_ID
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diaryandroid.presentation.common.NoMatchFound
import com.example.diaryandroid.presentation.destinations.WriteScreenDestination
import com.example.diaryandroid.presentation.write.WriteViewModel
import com.example.diaryandroid.util.GifImageLoader
import com.example.diaryandroid.util.Resource
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator?,
    viewModel: HomeViewModel = viewModel(),
    writeViewModel: WriteViewModel = viewModel(),
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val diaries by viewModel.diaries
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var signOutDialogOpened by remember { mutableStateOf(false) }
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(key1 = Unit){
        MongoDB.configureTheRealm()
    }
    NavigationDrawer(
       drawerState =drawerState,
       onSignOutClicked = {
           signOutDialogOpened = true
       }
   ) {
       Scaffold(
           modifier = Modifier
               .navigationBarsPadding()
               .statusBarsPadding()
               .nestedScroll(scrollBehaviour.nestedScrollConnection),
           topBar = {
               HomeTopBar(
                   scrollBehavior = scrollBehaviour,
                   onMenuClicked = {
                       scope.launch {
                           drawerState.open()
                       }
                   }
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                   onClick = {
                       navigator?.navigate(WriteScreenDestination())
                   }
               ) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = stringResource(R.string.new_diary_icon)
                   )
               }
           },
           content = { it ->
               padding = it
               when(diaries){
                   is Resource.Loading ->{
                       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                           GifImageLoader(
                               modifier = Modifier.size(250.dp),
                               resource = R.raw.diary_loading
                           )
                       }
                   }
                   is Resource.Success ->{
                       HomeContent(
                           paddingValues= padding,
                           diaryNotes =diaries.data!!,
                           onClick = {
                               val id =  diaries.data!!.values.first().first()._id
                               writeViewModel.updateDiaryId(id)
                               navigator?.navigate(WriteScreenDestination(
                                   title = diaries.data!!.values.map {diary->
                                       diary.first().title
                                   }.toString() ,
                                   description =  diaries.data!!.values.map {diary->
                                       diary.first().description
                                   }.toString(),
                                   mood = diaries.data!!.values.map { diary ->
                                       diary.first().mood
                                   }.toString(),

                                   ))
                           }
                       )
                   }
                   is Resource.Error ->{
                       NoMatchFound(lottie = R.raw.no_match_found_dark)

                   }
               }
           }
       )
   }
    DisplayAlertDialog(
        title = "Sign Out",
        message =" Are you sure you want to Sign Out from your Google account" ,
        dialogOpened = signOutDialogOpened,
        onDialogClosed = { signOutDialogOpened = false },
        onYesClicked = {
            scope.launch(Dispatchers.IO) {
                val user = App.create(APP_ID).currentUser
                if (user != null){
                    user.logOut()
                    withContext(Dispatchers.Main){
                        navigator?.popBackStack()
                        navigator?.navigate(AuthenticationScreenDestination)
                    }
                }
            }
        }
    )
}


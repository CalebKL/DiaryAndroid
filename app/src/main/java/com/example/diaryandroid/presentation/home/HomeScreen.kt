package com.example.diaryandroid.presentation.home

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.diaryandroid.R
import com.example.diaryandroid.presentation.destinations.AuthenticationScreenDestination
import com.example.diaryandroid.presentation.home.components.DisplayAlertDialog
import com.example.diaryandroid.presentation.home.components.HomeTopBar
import com.example.diaryandroid.presentation.home.components.NavigationDrawer
import com.example.diaryandroid.util.Constants.APP_ID
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var signOutDialogOpened by remember { mutableStateOf(false) }

    NavigationDrawer(
       drawerState =drawerState,
       onSignOutClicked = {
           signOutDialogOpened = true
       }
   ) {
       Scaffold(
           modifier = Modifier
               .navigationBarsPadding()
               .statusBarsPadding(),
           topBar = {
               HomeTopBar(
                   onMenuClicked = {
                       scope.launch {
                           drawerState.open()
                       }
                   }
               )
           },
           floatingActionButton = {
               FloatingActionButton(onClick = {}) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = stringResource(R.string.new_diary_icon)
                   )
               }
           },
           content = {
               val unUsedPadding = it
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


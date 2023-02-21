package com.example.diaryandroid.presentation.home

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
import com.example.diaryandroid.data.Diaries
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.presentation.common.NoMatchFound
import com.example.diaryandroid.presentation.home.components.HomeContent
import com.example.diaryandroid.presentation.home.components.HomeTopBar
import com.example.diaryandroid.presentation.home.components.NavigationDrawer
import com.example.diaryandroid.util.GifImageLoader
import com.example.diaryandroid.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onMenuClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    onDeleteAllClicked:()->Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(key1 = Unit){
        MongoDB.configureTheRealm()
    }
    NavigationDrawer(
        drawerState =drawerState,
        onSignOutClicked = onSignOutClicked,
        onDeleteAllClicked = onDeleteAllClicked
   ) {
       Scaffold(
           modifier = Modifier
               .navigationBarsPadding()
               .statusBarsPadding()
               .nestedScroll(scrollBehaviour.nestedScrollConnection),
           topBar = {
               HomeTopBar(
                   scrollBehavior = scrollBehaviour,
                   onMenuClicked = onMenuClicked
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                   onClick = navigateToWrite
               ) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = stringResource(R.string.new_diary_icon)
                   )
               }
           },
           content = {
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
                           diaryNotes =diaries.data,
                           onClick =navigateToWriteWithArgs
                       )
                   }
                   is Resource.Error ->{
                       NoMatchFound(lottie = R.raw.no_match_found_dark)

                   }else->{}
               }
           }
       )
   }
}


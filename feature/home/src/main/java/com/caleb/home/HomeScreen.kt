package com.caleb.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.caleb.home.components.EmptyPage
import com.caleb.util.common.NoMatchFound
import com.caleb.home.components.HomeContent
import com.caleb.home.components.HomeTopBar
import com.caleb.home.components.NavigationDrawer
import com.caleb.mongo.repository.Diaries
import com.caleb.mongo.repository.MongoDB
import com.caleb.util.GifImageLoader
import com.caleb.util.model.Resource
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onMenuClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    onDeleteAllClicked:()->Unit,
    dateIsSelected:Boolean,
    onDateSelected:(ZonedDateTime)-> Unit,
    onDateReset:()->Unit
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
               .nestedScroll(scrollBehaviour.nestedScrollConnection),
           topBar = {
               HomeTopBar(
                   scrollBehavior = scrollBehaviour,
                   onMenuClicked = onMenuClicked,
                   dateIsSelected = dateIsSelected,
                   onDateSelected= onDateSelected,
                   onDateReset = onDateReset
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                   onClick = navigateToWrite
               ) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = "Diary Icon"
                   )
               }
           },
           content = {
               padding = it
               when(diaries){
                   is Resource.Loading ->{
                       Box(
                           modifier = Modifier.fillMaxSize(),
                           contentAlignment = Alignment.Center
                       ) {
                           CircularProgressIndicator()
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
                       EmptyPage(
                           title = "Error",
                           subtitle = "${diaries.error.message}"
                       )
                   }else->{}
               }
           }
       )
   }
}


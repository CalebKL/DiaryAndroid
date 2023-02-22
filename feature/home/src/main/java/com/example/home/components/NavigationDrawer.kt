package com.example.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R

@Composable
internal fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked:()->Unit,
    onDeleteAllClicked:()->Unit,
    content:@Composable ()->Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center){
                        Image(
                            modifier = Modifier.size(250.dp),
                            painter = painterResource(id =  com.example.ui.R.drawable.logo),
                            contentDescription = "logo"
                        )
                    }
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)){
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = "Google Logo"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Sign Out",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected =false,
                        onClick = onSignOutClicked
                    )
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)){
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Icons"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Delete all Diaries",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected =false,
                        onClick = onDeleteAllClicked
                    )
                }
            )
        }, 
        content = content
    ) 
}
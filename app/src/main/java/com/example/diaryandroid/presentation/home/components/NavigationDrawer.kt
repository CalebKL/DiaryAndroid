package com.example.diaryandroid.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.diaryandroid.R

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked:()->Unit,
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
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(R.string.logo)
                        )
                    }
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)){
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = stringResource(R.string.google_logo)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = stringResource(R.string.sign_out),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected =false,
                        onClick = onSignOutClicked
                    )
                }
            )
        }, 
        content = content
    ) 
}
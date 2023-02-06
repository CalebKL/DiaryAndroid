package com.example.diaryandroid.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.diaryandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClicked:()->Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onMenuClicked() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.hamburger_menu_icon),
                    tint = MaterialTheme.colorScheme.onSurface

                )
            }
        },
        title = {
            Text(text = stringResource(R.string.diary))
        },
        actions = {
            IconButton(onClick = { onMenuClicked() }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.date_range),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
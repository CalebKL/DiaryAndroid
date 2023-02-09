package com.example.diaryandroid.presentation.write.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.diaryandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    onBackPressed:()->Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick =  onBackPressed ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.arrow_back))
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Happy",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "10 JAN 2023, 10:00AM",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                )
            }
        }
    )
}
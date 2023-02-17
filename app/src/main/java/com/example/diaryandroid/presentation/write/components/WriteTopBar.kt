package com.example.diaryandroid.presentation.write.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.diaryandroid.R
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.presentation.home.components.DisplayAlertDialog
import com.example.diaryandroid.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit,
    onBackPressed:()->Unit,
    moodName:()->String,
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedDate = remember(key1 = currentDate) {
        DateTimeFormatter
            .ofPattern("dd MMM yyyy")
            .format(currentDate).uppercase()
    }
    val formattedTime = remember(key1 = currentTime) {
        DateTimeFormatter
            .ofPattern("hh:mm a")
            .format(currentTime).uppercase()
    }
    var dateTimeUpdated by remember { mutableStateOf(false) }
    val selectedDiaryDateTime = remember(selectedDiary) {
        if (selectedDiary != null) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date.from(selectedDiary.date.toInstant())).uppercase()
        } else "Unknown"
    }
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick =  onBackPressed ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back)
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (selectedDiary != null && dateTimeUpdated) "$formattedDate, $formattedTime"
                    else if (selectedDiary != null) selectedDiaryDateTime
                    else "$formattedDate, $formattedTime",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    ),
                    textAlign = TextAlign.Center

                )
            }
        },
        actions = {
            IconButton(onClick =  {}) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.date_range),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (selectedDiary != null){
                DeleteDiaryAction(
                    selectedDiary = selectedDiary,
                    onDeleteConfirmed = onDeleteConfirmed
                )
            }
        }
    )
}


@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            }, onClick = {
                openDialog = true
                expanded = false
            }
        )
    }
    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this diary note '${selectedDiary?.title}'?",
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
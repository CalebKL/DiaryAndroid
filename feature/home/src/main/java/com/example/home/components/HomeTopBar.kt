package com.example.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked:()->Unit,
    dateIsSelected:Boolean,
    onDateSelected:(ZonedDateTime)-> Unit,
    onDateReset:()->Unit
) {
    val dateDialog = rememberSheetState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onMenuClicked() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint = MaterialTheme.colorScheme.onSurface

                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            if (dateIsSelected){
                IconButton(onClick = onDateReset) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }else{
                IconButton(onClick = {dateDialog.show()}) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription ="Date range",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
    CalendarDialog(
        state = dateDialog,
        selection =CalendarSelection.Date{localeDate->
            pickedDate = localeDate
            onDateSelected(
                ZonedDateTime.of(
                    pickedDate,
                    LocalTime.now(),
                    ZoneId.systemDefault()
                )
            )
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )
}
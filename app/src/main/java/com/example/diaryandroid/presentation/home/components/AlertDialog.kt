package com.example.diaryandroid.presentation.home.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.diaryandroid.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    dialogOpened: Boolean,
    onDialogClosed: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (dialogOpened) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        onDialogClosed()
                    })
                {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDialogClosed)
                {
                    Text(text = stringResource(R.string.no))
                }
            },
            onDismissRequest = onDialogClosed
        )
    }
}
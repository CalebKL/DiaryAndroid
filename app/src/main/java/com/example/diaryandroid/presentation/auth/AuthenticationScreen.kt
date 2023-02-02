package com.example.diaryandroid.presentation.auth

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diaryandroid.presentation.auth.components.AuthenticationContent
import com.example.diaryandroid.presentation.auth.components.AuthenticationViewModel
import com.example.diaryandroid.util.Constants.CLIENT_ID
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import timber.log.Timber
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun AuthenticationScreen(
    navigator: DestinationsNavigator?,
    oneTapSignInState: OneTapSignInState = rememberOneTapSignInState(),
    messageBarState: MessageBarState = rememberMessageBarState(),
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val loadingState by viewModel.loadingState
    Scaffold(
        content = {
            val unUsedPadding = it
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    onButtonClicked = {
                        oneTapSignInState.open()
                        viewModel.setLoading(true)
                    },
                    loadingState = loadingState
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId =CLIENT_ID ,
        onTokenIdReceived = { tokenId ->
            viewModel.signInWithMongoAtlas(
                tokenId= tokenId,
                onSuccess = {
                    if (it){
                        messageBarState.addSuccess("Successfully, Authenticated!")
                    }
                },
                onError = {
                    messageBarState.addError(it)
                }
            )
        },
        onDialogDismissed = {message->
            messageBarState.addError(Exception(message))
        }
    )
}
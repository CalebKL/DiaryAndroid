package com.example.diaryandroid.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.diaryandroid.presentation.auth.components.AuthenticationContent
import com.example.diaryandroid.presentation.destinations.HomeScreenDestination
import com.example.diaryandroid.util.Constants.CLIENT_ID
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun AuthenticationScreen(
    navigator: DestinationsNavigator?,
    oneTapSignInState: OneTapSignInState = rememberOneTapSignInState(),
    messageBarState: MessageBarState = rememberMessageBarState(),
    viewModel: AuthenticationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val authenticated by viewModel.authenticated
    val loadingState by viewModel.loadingState
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .statusBarsPadding(),
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
                    messageBarState.addSuccess("Successfully, Authenticated!")
                    viewModel.setLoading(false)
                },
                onError = {
                    messageBarState.addError(it)
                    viewModel.setLoading(false)
                }
            )
        },
        onDialogDismissed = {message->
            messageBarState.addError(Exception(message))
            viewModel.setLoading(false)
        }
    )
    LaunchedEffect(key1 = authenticated){
        if (authenticated){
            navigator?.popBackStack()
            navigator?.navigate(HomeScreenDestination)
        }
    }
}
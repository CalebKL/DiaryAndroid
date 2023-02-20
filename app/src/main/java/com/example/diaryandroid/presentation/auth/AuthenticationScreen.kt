package com.example.diaryandroid.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.diaryandroid.presentation.auth.components.AuthenticationContent
import com.example.diaryandroid.util.Constants.CLIENT_ID
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit,
    onSuccessfulFirebaseSignIn:(String)->Unit,
    onFailedFirebaseSignIn:(Exception)->Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .statusBarsPadding(),
        content = {
            val unUsedPadding = it
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    onButtonClicked = onButtonClicked,
                    loadingState = loadingState
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId =CLIENT_ID ,
        onTokenIdReceived = { tokenId ->
            val credentials = GoogleAuthProvider.getCredential(tokenId, null)
            FirebaseAuth.getInstance().signInWithCredential(credentials)
                .addOnCompleteListener{task->
                    if (task.isSuccessful){
                        onSuccessfulFirebaseSignIn(tokenId)
                    }else{
                        task.exception?.let { it -> onFailedFirebaseSignIn(it) }
                    }
                }
        },
        onDialogDismissed = onDialogDismissed
    )

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateToHome()
        }
    }
}
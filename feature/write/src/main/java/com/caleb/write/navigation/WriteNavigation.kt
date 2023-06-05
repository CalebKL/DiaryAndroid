package com.caleb.write.navigation

import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.caleb.util.Constants
import com.caleb.util.Screen
import com.caleb.util.model.Mood
import com.caleb.write.WriteScreen
import com.caleb.write.WriteViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(navigateBack: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel: WriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val context = LocalContext.current
        val galleryState = viewModel.galleryState
        val pagerState = rememberPagerState()
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        WriteScreen(
            galleryState = galleryState,
            uiState = uiState,
            pagerState = pagerState,
            moodName = { Mood.values()[pageNumber].name },
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onBackPressed = navigateBack,
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateBack()
                    },
                    onError = { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )},
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.values()[pageNumber].name},
                    onSuccess = navigateBack,
                    onError = {error->
                        Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDateTimeUpdated = {zonedDateTime ->
                viewModel.updateDateTime(zonedDateTime = zonedDateTime)
            },
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last()?:"jpg"
                viewModel.addImage(
                    image = it,
                    imageType =type
                )
            },
            onImageDeleteClicked = {galleryState.removeImage(it)}
        )
    }
}

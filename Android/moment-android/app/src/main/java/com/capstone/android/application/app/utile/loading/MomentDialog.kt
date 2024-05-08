package com.capstone.android.application.app.utile.loading

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun GlobalLoadingDialog() {
    val isLoading = LoadingState.isLoading.collectAsState().value

    if (isLoading) {
        Dialog(
            onDismissRequest = {  },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            CircularProgressIndicator()
        }
    }
}
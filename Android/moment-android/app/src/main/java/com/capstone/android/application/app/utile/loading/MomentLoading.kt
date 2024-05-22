package com.capstone.android.application.app.utile.loading

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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



@Composable
fun LinearDeterminateIndicator(
    currentProgress:MutableState<Float>, loading:MutableState<Boolean>,
    scope:CoroutineScope, onClick:()-> Unit, maxTime: Int
) {


    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {


            LinearProgressIndicator(
                progress = { currentProgress.value },
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                trackColor = Color("#938F8F".toColorInt())
            )
            if(loading.value){
                Log.d("waegwgwe","${loading.value}")
                    scope.launch {
                        loadProgress(loading=loading,maxTime = maxTime) { progress ->
                            currentProgress.value = progress

                        }
//                        loading.value = false // Reset loading when the coroutine finishes
                    }
                }

            }

    }

/** Iterate the progress value */
suspend fun loadProgress(loading:MutableState<Boolean>,maxTime:Int,updateProgress: (Float) -> Unit) {
    delay(1000)
    for (i in 1..maxTime) {
        if(!loading.value) break
        Log.d("awegdsagwegs","${i}")
        updateProgress(i.toFloat() / maxTime)
        delay(1000)

    }
    loading.value = false
    updateProgress(0f)
}
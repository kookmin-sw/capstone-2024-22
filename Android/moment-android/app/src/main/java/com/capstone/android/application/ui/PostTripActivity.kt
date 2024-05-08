package com.capstone.android.application.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.capstone.android.application.app.composable.MomentUiTripInfo
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.presentation.TripViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostTripActivity:ComponentActivity() {
    private val tripViewModel : TripViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent=intent
        tripViewModel.postTripRegisterSuccess.observe(this@PostTripActivity){
            setResult(1,mainIntent)
            finish()
        }

        setContent{
            val tripName = remember{
                mutableStateOf("")
            }
            val startDate = remember{
                mutableStateOf("출발 날짜")
            }

            val endDate = remember{
                mutableStateOf("도착 날짜")
            }

            MomentUiTripInfo(tripName = tripName, startDate = startDate, endDate = endDate, onClicked = {
                tripViewModel.postTripRegister(
                    body = PostTripRegisterRequest(
                        startDate=startDate.value,
                        endDate=endDate.value,
                        tripName=tripName.value
                    )

                )
            })
        }

    }

    @Preview
    @Composable
    fun PostTripActivityPreview(){
//        Main()
    }
}
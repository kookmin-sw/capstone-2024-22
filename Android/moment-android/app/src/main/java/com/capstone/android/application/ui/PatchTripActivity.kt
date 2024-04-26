package com.capstone.android.application.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.capstone.android.application.app.composable.MomentUiTripInfo
import com.capstone.android.application.data.remote.trip.model.trip_put.request.PutTripRequest
import com.capstone.android.application.presentation.TripViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class PatchTripActivity:ComponentActivity() {
    private val tripViewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent=intent
        tripViewModel.putTripSuccess.observe(this@PatchTripActivity){
            setResult(2,mainIntent)
            finish()
        }


        setContent{
            val tripId = remember {
                mutableStateOf(0)
            }
            val tripName = remember{
                mutableStateOf("")
            }
            val startDate = remember{
                mutableStateOf("출발 날짜")
            }

            val endDate = remember{
                mutableStateOf("도착 날짜")
            }

            try {
                mainIntent?.let {
                    tripId.value = it.getIntExtra("tripId",0)
                    startDate.value = it.getStringExtra("startDate")!!
                    endDate.value=it.getStringExtra("endDate")!!
                    tripName.value=it.getStringExtra("tripName")!!
                }

            }catch (e:Exception){
                Toast.makeText(this@PatchTripActivity,"server error",Toast.LENGTH_SHORT).show()
                finish()
            }






            MomentUiTripInfo(tripName = tripName, startDate = startDate, endDate = endDate, onClicked = {
                tripViewModel.putTrip(
                    body = PutTripRequest(
                        tripId = tripId.value,
                        startDate=startDate.value,
                        endDate=endDate.value,
                        tripName=tripName.value
                    )

                )
            })
        }
    }
}
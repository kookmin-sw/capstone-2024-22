package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.trip.TripRepository
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(private val tripRepository:TripRepository): ViewModel() {
    val tripAll : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    fun getTripAll(){
        viewModelScope.launch {
            try {
                val data = tripRepository.getTripAll()
                tripAll.postValue(data.message())

            } catch (e: HttpException) {
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                // Handle other generic exceptions
            }
        }
    }

    fun postTripRegister(
        body:PostTripRegisterRequest
    ){
        viewModelScope.launch {
            try {

                val data = tripRepository.postTripRegister(
                    body = body
                )
                Log.d("ewageawgewagewa",data.message())
                tripAll.postValue(data.message())


            } catch (e:HttpException){
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){

            } catch (e:Exception){

            }

        }
    }

    fun deleteTrip(
        userId:Int
    ){
        viewModelScope.launch {
            try {

                val data = tripRepository.deleteTrip(
                    userId = userId
                )


            } catch (e:HttpException){
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){

            } catch (e:Exception){

            }

        }
    }

    fun patchTrip(
        userId : Int,
        body : PostTripPatchRequest
    ){
        viewModelScope.launch {
            try {

                val data = tripRepository.patchTrip(
                    userId = userId,
                    body = body
                )


            } catch (e:HttpException){
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){

            } catch (e:Exception){

            }

        }
    }
}
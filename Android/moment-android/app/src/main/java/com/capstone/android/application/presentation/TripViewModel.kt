package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.trip.TripRepository
import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_patch.request.PostTripPatchRequest
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(private val tripRepository:TripRepository): ViewModel() {

    // 여행목록 조회 성공
    val getTripAllSuccess : MutableLiveData<GetTripAllResponse> by lazy{
        MutableLiveData<GetTripAllResponse>()
    }

    // 여행목록 조회 실패
    val getTripAllFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    // 여행등록 성공
    val postTripRegisterSuccess : MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    // 여행등록 실패
    val postTripRegisterFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    // 여행삭제 성공
    val deleteTripSuccess : MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    // 여행삭제 실패
    val deleteTripFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    // 여행수정 성공
    val patchTripSuccess : MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    // 여행수정 실패
    val patchTripFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    fun getTripAll(){
        viewModelScope.launch {
            try {
                val response = tripRepository.getTripAll()
                if(response is ApiResponse.Success){
                    getTripAllSuccess.postValue(response.data)
                }else{

                }

            } catch (e: HttpException) {
                getTripAllFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                getTripAllFailure.postValue(ApiResponse.Error(e))

                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                getTripAllFailure.postValue(ApiResponse.Error(e))

                // Handle other generic exceptions
            }
        }
    }

    fun postTripRegister(
        body:PostTripRegisterRequest
    ){
        viewModelScope.launch {
            try {

                val response = tripRepository.postTripRegister(
                    body = body
                )

                if(response is ApiResponse.Success){
                    postTripRegisterSuccess.postValue(response.data)
                }else{

                }


            } catch (e:HttpException){
                postTripRegisterFailure.postValue(ApiResponse.Error(e))
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){
                postTripRegisterFailure.postValue(ApiResponse.Error(e))

            } catch (e:Exception){
                postTripRegisterFailure.postValue(ApiResponse.Error(e))

            }

        }
    }

    fun deleteTrip(
        userId:Int
    ){
        viewModelScope.launch {
            try {

                val response = tripRepository.deleteTrip(
                    userId = userId
                )

                if(response is ApiResponse.Success){
                    deleteTripSuccess.postValue(response.data)
                }else{

                }


            } catch (e:HttpException){
                deleteTripFailure.postValue(ApiResponse.Error(e))
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){
                deleteTripFailure.postValue(ApiResponse.Error(e))


            } catch (e:Exception){
                deleteTripFailure.postValue(ApiResponse.Error(e))

            }

        }
    }

    fun patchTrip(
        userId : Int,
        body : PostTripPatchRequest
    ){
        viewModelScope.launch {
            try {

                val response = tripRepository.patchTrip(
                    userId = userId,
                    body = body
                )
                if(response is ApiResponse.Success){
                    patchTripSuccess.postValue(response.data)
                }else{

                }


            } catch (e:HttpException){
                patchTripFailure.postValue(ApiResponse.Error(e))
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){
                patchTripFailure.postValue(ApiResponse.Error(e))
            } catch (e:Exception){
                patchTripFailure.postValue(ApiResponse.Error(e))
            }

        }
    }
}
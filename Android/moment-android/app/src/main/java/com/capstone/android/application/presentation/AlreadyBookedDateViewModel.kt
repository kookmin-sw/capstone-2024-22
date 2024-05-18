package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.already_booked_date.AlreadyBookedDateRepository
import com.capstone.android.application.data.remote.already_booked_date.model.GetAleadyBookedDateAllResponse
import com.capstone.android.application.data.remote.trip.TripRepository
import com.capstone.android.application.data.remote.trip.model.trip_all.GetTripAllResponse
import com.capstone.android.application.data.remote.trip.model.trip_detail.GetTripDetailResponse
import com.capstone.android.application.data.remote.trip.model.trip_register.request.PostTripRegisterRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AlreadyBookedDateViewModel @Inject constructor(private val alreadyBookedDateRepository:AlreadyBookedDateRepository): ViewModel() {

    // 여행목록 조회 성공
    val getAlreadyBookedDateAllSuccess : MutableLiveData<GetAleadyBookedDateAllResponse> by lazy{
        MutableLiveData<GetAleadyBookedDateAllResponse>()
    }

    // 여행목록 조회 실패
    val getAlreadyBookedDateAllFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }



    fun getAlreadyBookedDateAll(){
        viewModelScope.launch {
            try {

                val response = alreadyBookedDateRepository.getAlreadyBookedDateAll()
                if(response is ApiResponse.Success){
                    getAlreadyBookedDateAllSuccess.postValue(response.data)
                }else{

                }

            } catch (e: HttpException) {
                getAlreadyBookedDateAllFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                getAlreadyBookedDateAllFailure.postValue(ApiResponse.Error(e))

                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                getAlreadyBookedDateAllFailure.postValue(ApiResponse.Error(e))

                // Handle other generic exceptions
            }
        }
    }



}
package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.tripfile.TripFileRepository
import com.capstone.android.application.domain.response.trip_file.TripFileResponse
import com.capstone.android.application.domain.response.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TripFileViewModel @Inject constructor(private val tripFileRepository: TripFileRepository):ViewModel() {

    val getTripFileSuccess : MutableLiveData<TripFileResponse> by lazy {
        MutableLiveData<TripFileResponse>()
    }

    val getTripFileFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    val getTripFileUntitledSuccess : MutableLiveData<TripFileResponse> by lazy {
        MutableLiveData<TripFileResponse>()
    }

    val getTripFileUntitledFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    fun getTripFileAll(tripId:Int){
        viewModelScope.launch {
            try {
                val response = tripFileRepository.getTripFileAll(tripId = tripId)
                if(response is ApiResponse.Success){
                    getTripFileSuccess.postValue(response.data)
                }

            } catch (e: HttpException){
                getTripFileFailure.postValue(ApiResponse.Error(e))

            } catch (e: IOException){
                getTripFileFailure.postValue(ApiResponse.Error(e))
            } catch (e:Exception){
                getTripFileFailure.postValue(ApiResponse.Error(e))
            }

        }

    }

    fun getTripFileUntitled(){
        viewModelScope.launch {
            try {
                val response = tripFileRepository.getTripFileUntitled()
                if(response is ApiResponse.Success){
                    getTripFileUntitledSuccess.postValue(response.data)
                }

            } catch (e: HttpException){
                getTripFileUntitledFailure.postValue(ApiResponse.Error(e))

            } catch (e: IOException){
                getTripFileUntitledFailure.postValue(ApiResponse.Error(e))

            } catch (e:Exception){
                getTripFileUntitledFailure.postValue(ApiResponse.Error(e))
            }

        }
    }

}


package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.open_weather.OpenWeatherRepository
import com.capstone.android.application.data.remote.open_weather.model.response.GetWeatherResponse
import com.capstone.android.application.domain.response.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OpenWeatherViewModel @Inject constructor(private val openWeatherRepository: OpenWeatherRepository): ViewModel() {
    val getWeatherInCurrentLocationlSuccess : MutableLiveData<GetWeatherResponse> by lazy {
        MutableLiveData<GetWeatherResponse>()
    }

    val getWeatherInCurrentLocationFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    fun getWeatherInCurrentLocation(
        lat : String,
        lon : String,
        appid : String
    ){
        viewModelScope.launch {
            try {
                val response = openWeatherRepository.getWeatherInCurrentLocation(
                    lat = lat,
                    lon = lon,
                    appid = appid
                )

                if(response is ApiResponse.Success){
                    getWeatherInCurrentLocationlSuccess.postValue(response.data)
                }

            }catch (e: HttpException){
                getWeatherInCurrentLocationFailure.postValue(ApiResponse.Error(e))

            }catch (e: IOException){
                getWeatherInCurrentLocationFailure.postValue(ApiResponse.Error(e))

            }catch (e:Exception){
                getWeatherInCurrentLocationFailure.postValue(ApiResponse.Error(e))
            }
        }
    }
}
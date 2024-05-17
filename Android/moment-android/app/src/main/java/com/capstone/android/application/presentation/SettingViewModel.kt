package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.auth.AuthRepository
import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.data.remote.auth.login.response.PostAuthLoginResponse
import com.capstone.android.application.data.remote.setting.SettingRepository
import com.capstone.android.application.data.remote.setting.model.PatchFcmTokenRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.auth.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val settingRepository: SettingRepository): ViewModel() {
    val patchFcmTokenSuccess : MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    val patchFcmFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    fun patchFcmToken(
        body : PatchFcmTokenRequest
    ){
        viewModelScope.launch {
            try {
                val response = settingRepository.patchFcmToken(
                    body = body
                )


                if(response is ApiResponse.Success){
                    patchFcmTokenSuccess.value = response.data
                    Log.d("awegawgaew","sss")
                }else{

//                    val error = (data.body() as MomentNetworkError)
//                    authFailure.value=MomentNetworkError(
//                        errors =
//                    )
                }


            } catch (e: HttpException) {

                // Handle specific HTTP error codes
                when (e.code()) {
                    400 -> {
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

}
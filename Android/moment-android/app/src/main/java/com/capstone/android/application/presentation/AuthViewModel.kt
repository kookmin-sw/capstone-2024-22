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
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.auth.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    val postAuthLoginSuccess : MutableLiveData<PostAuthLoginResponse> by lazy {
        MutableLiveData<PostAuthLoginResponse>()
    }

    val postAuthLoginFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    val patchAuthChangePasswordSuccess : MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    val patchAuthChangePasswordFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    val postAuthAuthCodeSuccess : MutableLiveData<AuthResponse> by lazy {
        MutableLiveData<AuthResponse>()
    }

    val postAuthAuthCodeFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    val patchAuthAuthCodeConfirmSuccess:MutableLiveData<AuthResponse> by lazy {
        MutableLiveData<AuthResponse>()
    }
    val patchAuthAuthCodeConfirmFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }



    fun postAuthLogin(
        body : PostAuthLoginRequest
    ){
        viewModelScope.launch {
            try {
                val response = authRepository.postAuthLogin(
                    body = body
                )


                if(response is ApiResponse.Success){
                    Log.d("waegwgwea","HttpException")
                    postAuthLoginSuccess.value = response.data
                }else{
                    Log.d("waegwgwea","HttpException")
                    postAuthLoginFailure.postValue(ApiResponse.Error(exception = java.lang.Exception()))

//                    val error = (data.body() as MomentNetworkError)
//                    authFailure.value=MomentNetworkError(
//                        errors =
//                    )
                }


            } catch (e: HttpException) {
                Log.d("waegwgwea","HttpException")

                // Handle specific HTTP error codes
                when (e.code()) {
                    400 -> {
                        postAuthLoginFailure.postValue(ApiResponse.Error(exception = e))
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("waegwgwea","IOException")

                postAuthLoginFailure.postValue(ApiResponse.Error(exception = e))


                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("waegwgwea","Exception")

                postAuthLoginFailure.postValue(ApiResponse.Error(exception = e))

                // Handle other generic exceptions
            }
        }
    }

    fun patchAuthChangePassword(
        body:PatchAuthChangePasswordRequest
    ){
        viewModelScope.launch {
            try {

                val response = authRepository.patchAuthCodeAChangePassword(
                    body = body
                )
                if(response is ApiResponse.Success){
                    patchAuthChangePasswordSuccess.value=response.data

                }else{

//                    val error = (data.body() as MomentNetworkError)
//                    authFailure.value=MomentNetworkError(
//                        errors =
//                    )
                }




            } catch (e:HttpException){
//                authFailure.value=MomentNetworkError(code = e.code(),content = e.message())
                patchAuthChangePasswordFailure.postValue(ApiResponse.Error(exception = e))
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){

            } catch (e:Exception){

            }

        }
    }

    fun postAuthAuthCode(
        body : PostAuthAuthCodeRequest
    ){
        viewModelScope.launch {
            try {

                val response = authRepository.postAuthAuthCode(
                    body = body
                )
                if(response is ApiResponse.Success){
                    postAuthAuthCodeSuccess.value = response.data
                }else{

//                    val error = (data.body() as MomentNetworkError)
//                    authFailure.value=MomentNetworkError(
//                        errors =
//                    )
                }




            } catch (e:HttpException){
                postAuthAuthCodeFailure.postValue(ApiResponse.Error(exception = e))
                when(e.code()){
                    404 -> {

                    }
                }

            } catch (e:IOException){

            } catch (e:Exception){

            }

        }
    }

    fun patchAuthAuthCodeConfirm(
        body : PatchAuthAuthCodeConfirmRequest
    ){
        viewModelScope.launch {
            try {

                val response = authRepository.patchAuthAuthCodeConfirm(
                    body = body
                )

                if(response is ApiResponse.Success){
                    patchAuthAuthCodeConfirmSuccess.value = response.data
                }else{

//                    val error = (data.body() as MomentNetworkError)
//                    authFailure.value=MomentNetworkError(
//                        errors =
//                    )
                }


            } catch (e:HttpException){
                patchAuthAuthCodeConfirmFailure.postValue(ApiResponse.Error(exception = e))
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
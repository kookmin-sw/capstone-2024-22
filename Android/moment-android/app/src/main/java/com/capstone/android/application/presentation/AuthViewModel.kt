package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.auth.AuthRepository
import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun postAuthLogin(
        body : PostAuthLoginRequest
    ){
        viewModelScope.launch {
            try {
                val data = authRepository.postAuthLogin(
                    body = body
                )

            } catch (e: HttpException) {
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        Log.d("awegawegewa","404")
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegewa","IOE")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegewa","${e.message}")
                // Handle other generic exceptions
            }
        }
    }

    fun patchAuthChangePassword(
        body:PatchAuthChangePasswordRequest
    ){
        viewModelScope.launch {
            try {

                val data = authRepository.patchAuthCodeAChangePassword(
                    body = body
                )
                Log.d("ewageawgewagewa",data.message())


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

    fun postAuthAuthCode(
        body : PostAuthAuthCodeRequest
    ){
        viewModelScope.launch {
            try {

                val data = authRepository.postAuthAuthCode(
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

    fun patchAuthAuthCodeConfirm(
        body : PatchAuthAuthCodeConfirmRequest
    ){
        viewModelScope.launch {
            try {

                val data = authRepository.patchAuthAuthCodeConfirm(
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
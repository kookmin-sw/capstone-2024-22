package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.receipt.ReceiptRepository
import com.capstone.android.application.data.remote.receipt.model.receipt_all.getReceiptAllResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_count.getReceiptCountResponse
import com.capstone.android.application.data.remote.receipt.model.receipt_delete.deleteReceiptDeleteRequest
import com.capstone.android.application.data.remote.receipt.model.receipt_post.PostReceiptCreateRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(private val receiptRepository: ReceiptRepository):
    ViewModel() {

    //영수증 생성 성공
    val  postReceiptCreateSuccess : MutableLiveData<MomentResponse> by lazy{
        MutableLiveData<MomentResponse>()
    }

    //영수증 생성 실패
    val  postReceiptCreateFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy{
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    //영수증 삭제 성공
    val  deleteReceiptDeleteSuccess : MutableLiveData<MomentResponse> by lazy{
        MutableLiveData<MomentResponse>()
    }

    //영수증 삭제 실패
    val  deleteReceiptDeleteFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy{
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    //영수증 모아보기 데이터 받기 성공
    val  getReceiptAllSuccess : MutableLiveData<getReceiptAllResponse> by lazy{
        MutableLiveData<getReceiptAllResponse>()
    }

    //영수증 모아보기 데이터 받기 실패
    val  getReceiptAllFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    //영수증 개수 받기 성공
    val getReceiptCountSuccess : MutableLiveData<getReceiptCountResponse> by lazy{
        MutableLiveData<getReceiptCountResponse>()
    }

    //영수증 개수 받기 실패
    val getReceiptCountFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    fun postReceiptCreate(
        body : PostReceiptCreateRequest
    ){
        viewModelScope.launch {
            try {
                val response = receiptRepository.postReceiptCreate(body = body)

                if (response is ApiResponse.Success){
                    postReceiptCreateSuccess.postValue(response.data)
                }else{

                }
            } catch (e: HttpException) {
                Log.d("qwer_postReceiptCreate", "404")
                postReceiptCreateFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            }   catch (e: IOException) {
                Log.d("qwer_postReceiptCreate","${e.message}")
                postReceiptCreateFailure.postValue(ApiResponse.Error(e))
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("qwer_postReceiptCreate","${e.message}")
                postReceiptCreateFailure.postValue(ApiResponse.Error(e))
                // Handle other generic exceptions
            }
        }
    }

    fun deleteReceiptDelete(
        body : deleteReceiptDeleteRequest
    ){
        viewModelScope.launch {
            try {
                val response = receiptRepository.deleteReceiptDelete(body = body)

                if (response is ApiResponse.Success){
                    deleteReceiptDeleteSuccess.postValue(response.data)
                }else{

                }
            } catch (e: HttpException) {
                Log.d("qwer_deleteReceiptDelete", "404")
                deleteReceiptDeleteFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            }   catch (e: IOException) {
                Log.d("qwer_deleteReceiptDelete","${e.message}")
                deleteReceiptDeleteFailure.postValue(ApiResponse.Error(e))
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("qwer_deleteReceiptDelete","${e.message}")
                deleteReceiptDeleteFailure.postValue(ApiResponse.Error(e))
                // Handle other generic exceptions
            }
        }
    }

    fun getReceiptAll(
        page : Int,
        size : Int
    ){
        viewModelScope.launch {
            try {
                val response = receiptRepository.getReceiptAll(page, size)

                if (response is ApiResponse.Success){
                    getReceiptAllSuccess.postValue(response.data)
                }else{

                }
            } catch (e: HttpException) {
                Log.d("qwer_getReceiptAll", "404")
                getReceiptAllFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            }   catch (e: IOException) {
                Log.d("qwer_getReceiptAll","${e.message}")

            } catch (e: Exception) {
                Log.d("qwer_getReceiptAll","${e.message}")
            }
        }
    }

    fun getReceiptCount(){
        viewModelScope.launch {
            try {
                val response = receiptRepository.getReceiptCount()

                if (response is ApiResponse.Success){
                    getReceiptCountSuccess.postValue(response.data)

                }else{

                }
            } catch (e: HttpException) {
                Log.d("qwer_getReceiptCount", "404")
                getReceiptCountFailure.postValue(ApiResponse.Error(e))
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            }   catch (e: IOException) {
                Log.d("qwer_getReceiptCount","${e.message}")
//                getReceiptCountFailure.postValue(ApiResponse.Error(e))
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("qwer_getReceiptCount","${e.message}")
//                getReceiptCountFailure.postValue(ApiResponse.Error(e))
                // Handle other generic exceptions
            }
        }
    }

}
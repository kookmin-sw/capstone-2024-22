package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.card.CardRepository
import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import com.capstone.android.application.data.remote.card.model.card_post.response.PostCardUploadResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.card.CardResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(private val cardRepository: CardRepository): ViewModel() {

    // 카드업로드 성공
    val postCardUploadSuccess : MutableLiveData<PostCardUploadResponse> by lazy{
        MutableLiveData<PostCardUploadResponse>()
    }

    // 카드업로드 실패
    val postCardUploadFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }


    // 카드조회 성공
    val getCardAllSuccess : MutableLiveData<CardResponse> by lazy{
        MutableLiveData<CardResponse>()
    }

    // 카드조회 실패
    val getCardAllFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    val getCardLikedSuccess : MutableLiveData<CardResponse> by lazy {
        MutableLiveData<CardResponse>()
    }

    val getCardLikedFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    val putCardLikeSuccess:MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    val deleteCardSuccess:MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    val deleteCardFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    val postCardImageUploadSuccess:MutableLiveData<MomentResponse> by lazy {
        MutableLiveData<MomentResponse>()
    }

    val postCardImageUploadFailure:MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    fun postCardUpload(
        cardUploadMultipart:RequestBody,
        recordFile : MultipartBody.Part
    ){
        viewModelScope.launch {
            try {
                val response = cardRepository.postCardUpload(
                    cardUploadMultipart = cardUploadMultipart,
                    recordFile = recordFile
                )
                if(response is ApiResponse.Success){
                    postCardUploadSuccess.postValue(response.data)
                }else{

                }

            }catch (e:HttpException){
                postCardUploadFailure.postValue(ApiResponse.Error(e))

            }catch (e:IOException){
                postCardUploadFailure.postValue(ApiResponse.Error(e))

            }catch (e:Exception){
                postCardUploadFailure.postValue(ApiResponse.Error(e))
            }
        }


    }

    fun deleteCard(
        cardViewId : Int
    ){
        viewModelScope.launch {
            try {

                val response = cardRepository.deleteCard(
                    cardViewId = cardViewId
                )

                if(response is ApiResponse.Success){
                    deleteCardSuccess.value=response.data
                }

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

    fun getCardAll(tripFileId:Int){
        viewModelScope.launch {
            try {
                val response = cardRepository.getCardAll(tripFileId = tripFileId)

                if(response is ApiResponse.Success){
                    getCardAllSuccess.postValue(response.data)
                }else{

                }

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

    fun putCardModify(
        cardViewId : Int,
        body:PutCardModifyRequest
    ){
        viewModelScope.launch {
            try {
                val data = cardRepository.putCardModify(
                    cardViewId = cardViewId,
                    body=body
                )

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

    fun putCardLike(
        cardViewId : Int
    ){
        viewModelScope.launch {
            try {
                val data = cardRepository.putCardLike(
                    cardViewId = cardViewId
                )

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

    fun getCardLiked(){
        viewModelScope.launch {
            try {
                val response = cardRepository.getCardLiked()

                if(response is ApiResponse.Success){
                    getCardLikedSuccess.postValue(response.data)
                }else{

                }



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

    fun postCardImageUpload(
        cardViewId : Int,
        uploadImageList:ArrayList<MultipartBody.Part>
    ){
        viewModelScope.launch {
            try {

                val response = cardRepository.postCardImageUpload(
                    cardViewId = cardViewId,
                    uploadImageList = uploadImageList
                )


                if(response is ApiResponse.Success){
                    postCardImageUploadSuccess.value = response.data
                }else{

                }



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

}
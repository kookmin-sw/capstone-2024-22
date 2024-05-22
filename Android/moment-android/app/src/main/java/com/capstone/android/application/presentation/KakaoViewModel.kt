package com.capstone.android.application.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.kakao.KakaoRepository
import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.domain.response.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class KakaoViewModel @Inject constructor(private val kakaoRepository: KakaoRepository): ViewModel() {
    val getLocalSuccess : MutableLiveData<GetKakaoLocalResponse> by lazy {
        MutableLiveData<GetKakaoLocalResponse>()
    }

    val getLocalFailure : MutableLiveData<ApiResponse.Error<Exception>> by lazy {
        MutableLiveData<ApiResponse.Error<Exception>>()
    }

    fun getLocal(
        x : String,
        y : String
    ){
        viewModelScope.launch {
            try {
                val response = kakaoRepository.getLocal( x = x, y = y)

                if(response is ApiResponse.Success){
                    getLocalSuccess.postValue(response.data)
                }

            }catch (e: HttpException){
                getLocalFailure.postValue(ApiResponse.Error(e))

            }catch (e: IOException){
                getLocalFailure.postValue(ApiResponse.Error(e))

            }catch (e:Exception){
                getLocalFailure.postValue(ApiResponse.Error(e))
            }
        }
    }
}
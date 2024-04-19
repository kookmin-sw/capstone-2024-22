package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.card.CardRepository
import com.capstone.android.application.data.remote.card.model.card_modify.request.PutCardModifyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(private val cardRepository: CardRepository): ViewModel() {

    fun deleteCard(
        cardViewId : Int
    ){
        viewModelScope.launch {
            try {
                val data = cardRepository.deleteCard(
                    cardViewId = cardViewId
                )

            } catch (e: HttpException) {
                Log.d("awegawegaew","404")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","${e.message}")
                // Handle other generic exceptions
            }
        }
    }

    fun getCardAll(){
        viewModelScope.launch {
            try {
                val data = cardRepository.getCardAll()

            } catch (e: HttpException) {
                Log.d("awegawegaew","404")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","${e.message}")
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
                Log.d("awegawegaew","404")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","${e.message}")
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
                Log.d("awegawegaew","404")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","${e.message}")
                // Handle other generic exceptions
            }
        }
    }

    fun getCardLiked(){
        viewModelScope.launch {
            try {
                val data = cardRepository.getCardLiked()

            } catch (e: HttpException) {
                Log.d("awegawegaew","404")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","${e.message}")
                // Handle other generic exceptions
            }
        }
    }


}
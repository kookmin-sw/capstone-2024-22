package com.capstone.android.application.data.remote.kakao

import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.domain.response.ApiResponse

interface KakaoRepositoryInterface {

    suspend fun getLocal(
        x:String,
        y:String
    ):ApiResponse<GetKakaoLocalResponse>


}
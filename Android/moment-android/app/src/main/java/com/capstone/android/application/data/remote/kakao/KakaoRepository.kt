package com.capstone.android.application.data.remote.kakao

import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.domain.response.ApiResponse
import javax.inject.Inject

class KakaoRepository @Inject constructor(private val kakaoRetrofitInterface: KakaoRetrofitInterface):KakaoRepositoryInterface {
    override suspend fun getLocal(x: String, y: String): ApiResponse<GetKakaoLocalResponse> {
        return kakaoRetrofitInterface.getLocal(
            x = x,
            y = y
        )
    }
}
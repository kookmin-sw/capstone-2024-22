package com.capstone.android.application.data.remote.kakao

import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.domain.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoRetrofitInterface {

    @GET("v2/local/geo/coord2regioncode.json")
    suspend fun getLocal(
        @Query("x") x:String,
        @Query("y") y:String): ApiResponse<GetKakaoLocalResponse>
}

package com.capstone.android.application.data.remote.setting

import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.data.remote.setting.model.PatchFcmTokenRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface SettingRetrofitInterface {

    @PATCH("core/user/setting")
    suspend fun patchFcmToken(
        @Body body: PatchFcmTokenRequest
    ): ApiResponse<MomentResponse>
}

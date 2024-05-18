package com.capstone.android.application.data.remote.setting

import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.data.remote.setting.model.PatchFcmTokenRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.http.Body

interface SettingRepositoryInterface {
    suspend fun patchFcmToken(
        body: PatchFcmTokenRequest
    ):ApiResponse<MomentResponse>

}
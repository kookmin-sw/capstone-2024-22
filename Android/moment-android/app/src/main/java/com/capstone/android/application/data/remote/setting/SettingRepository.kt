package com.capstone.android.application.data.remote.setting

import com.capstone.android.application.data.remote.kakao.model.kakao_local_get.response.GetKakaoLocalResponse
import com.capstone.android.application.data.remote.setting.model.PatchFcmTokenRequest
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import javax.inject.Inject

class SettingRepository @Inject constructor(private val settingRetrofitInterface: SettingRetrofitInterface):SettingRepositoryInterface {
    override suspend fun patchFcmToken(body: PatchFcmTokenRequest): ApiResponse<MomentResponse> {
        return settingRetrofitInterface.patchFcmToken(
            body = body
        )
    }

}
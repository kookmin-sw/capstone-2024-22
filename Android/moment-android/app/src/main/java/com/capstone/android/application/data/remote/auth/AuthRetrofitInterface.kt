package com.capstone.android.application.data.remote.auth

import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code.response.PostAuthAuthCodeResponse
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.data.remote.auth.login.response.PostAuthLoginResponse
import com.capstone.android.application.domain.response.MomentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/auth/login")
    suspend fun postAuthLogin(
        @Body body:PostAuthLoginRequest
    ) : Response<PostAuthLoginResponse>

    @PATCH("/auth/password")
    suspend fun patchAuthChangePassword(
        @Body body:PatchAuthChangePasswordRequest
    ) : Response<MomentResponse>

    @POST("/auth/code")
    suspend fun postAuthAuthCode(
        @Body body:PostAuthAuthCodeRequest
    ):Response<PostAuthAuthCodeResponse>

    @PATCH("/auth/verify")
    suspend fun patchAuthAuthCodeConfirm(
        @Body body:PatchAuthAuthCodeConfirmRequest
    ):Response<MomentResponse>


}
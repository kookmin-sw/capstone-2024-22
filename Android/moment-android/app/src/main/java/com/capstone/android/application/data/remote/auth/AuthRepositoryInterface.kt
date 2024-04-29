package com.capstone.android.application.data.remote.auth

import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.data.remote.auth.login.response.PostAuthLoginResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.auth.AuthResponse

interface AuthRepositoryInterface {

    suspend fun postAuthLogin(
        body : PostAuthLoginRequest
    ) : ApiResponse<PostAuthLoginResponse>
    suspend fun patchAuthCodeAChangePassword(
        body: PatchAuthChangePasswordRequest
    ) : ApiResponse<MomentResponse>

    suspend fun postAuthAuthCode(
        body : PostAuthAuthCodeRequest
    ) : ApiResponse<AuthResponse>

    suspend fun patchAuthAuthCodeConfirm(
        body : PatchAuthAuthCodeConfirmRequest
    ) : ApiResponse<AuthResponse>

}
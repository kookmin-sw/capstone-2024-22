package com.capstone.android.application.data.remote.auth

import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.data.remote.auth.login.response.PostAuthLoginResponse
import com.capstone.android.application.domain.response.ApiResponse
import com.capstone.android.application.domain.response.MomentResponse
import com.capstone.android.application.domain.response.auth.AuthResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authRetrofitInterface: AuthRetrofitInterface) : AuthRepositoryInterface{
    override suspend fun postAuthLogin(
        body: PostAuthLoginRequest
    ): ApiResponse<PostAuthLoginResponse> {
        return authRetrofitInterface.postAuthLogin(
            body = body
        )
    }

    override suspend fun patchAuthCodeAChangePassword(
        body: PatchAuthChangePasswordRequest
    ): ApiResponse<MomentResponse> {
        return authRetrofitInterface.patchAuthChangePassword(
            body = body
        )
    }

    override suspend fun postAuthAuthCode(
        body: PostAuthAuthCodeRequest
    ): ApiResponse<AuthResponse> {
        return authRetrofitInterface.postAuthAuthCode(
            body = body
        )
    }

    override suspend fun patchAuthAuthCodeConfirm(
        body: PatchAuthAuthCodeConfirmRequest
    ): ApiResponse<AuthResponse> {
        return authRetrofitInterface.patchAuthAuthCodeConfirm(
            body = body
        )

    }
}
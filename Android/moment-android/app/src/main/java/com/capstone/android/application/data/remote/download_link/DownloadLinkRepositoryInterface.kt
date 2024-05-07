package com.capstone.android.application.data.remote.download_link

import okhttp3.ResponseBody
import retrofit2.Response

interface DownloadLinkRepositoryInterface {
    suspend fun downloadFileFromUrl(
        url:String
    ): Response<ResponseBody>
}
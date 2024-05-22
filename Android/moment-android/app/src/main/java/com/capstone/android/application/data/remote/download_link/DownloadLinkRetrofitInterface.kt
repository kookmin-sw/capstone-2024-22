package com.capstone.android.application.data.remote.download_link

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadLinkRetrofitInterface {
    @Streaming
    @GET
    suspend fun downloadFileFromUrl(
        @Url url:String
    ): Response<ResponseBody>
}
package com.capstone.android.application.data.remote.download_link

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DownloadLinkRepository @Inject constructor(private val downloadLinkRetrofitInterface: DownloadLinkRetrofitInterface):
    DownloadLinkRepositoryInterface {
    override suspend fun downloadFileFromUrl(url: String): Response<ResponseBody> {
        return downloadLinkRetrofitInterface.downloadFileFromUrl(
            url = url
        )
    }
}
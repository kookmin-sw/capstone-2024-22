package com.capstone.android.application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.android.application.data.remote.download_link.DownloadLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class DownloadLinkViewModel @Inject constructor(private val downloadLinkRepository: DownloadLinkRepository): ViewModel() {

    fun downloadFileFromUrl(url:String){
        viewModelScope.launch {
            try {
                val response = downloadLinkRepository.downloadFileFromUrl(url=url)

                saveFile(
                    body = response.body(),
                    pathWhereYouWantToSaveFile = "/data/data/com.capstone.android.application/record/",
                    fileName = url.split("/").last().split(".").first()+".mp3"
                )

            } catch (e: HttpException) {
                Log.d("awegawegaew","HttpException : ${e.message()}")
                // Handle specific HTTP error codes
                when (e.code()) {
                    404 -> {
                        // Handle resource not found error
                    }
                    // Handle other error codes
                }
            } catch (e: IOException) {
                Log.d("awegawegaew","IOException: ${e.message}")
                // Handle network-related errors
//                throw NetworkException("Network error occurred", e)
            } catch (e: Exception) {
                Log.d("awegawegaew","Exception: ${e.message}")
                // Handle other generic exceptions
            }
        }

    }

    fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String,fileName:String):String{

        if (body==null)
            return ""
        var input: InputStream? = null
        try {

            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile+fileName)

            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.d("waegagewa","${e.message}")
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

}
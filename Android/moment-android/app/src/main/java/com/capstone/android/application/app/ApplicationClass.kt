package com.capstone.android.application.app

import android.app.Application
import android.content.SharedPreferences

import dagger.hilt.android.HiltAndroidApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class ApplicationClass: Application() {
    companion object{
        lateinit var tokenSharedPreferences: SharedPreferences

        lateinit var retrofit: Retrofit
        lateinit var retrofitKakao: Retrofit

        val API_URL="http://211.205.171.117:8000/"
        val KAKAO_LOCAL_API_URL = "https://dapi.kakao.com/"
    }



    override fun onCreate() {
        super.onCreate()
        tokenSharedPreferences =
            applicationContext.getSharedPreferences("TOKEN", MODE_PRIVATE)

        initRetrofitInstance()
//        디버그 모드일 때 적용
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }


        if(true){
            Timber.plant(Timber.DebugTree())
            Timber.i("ApplicationClass Success")
        }else{
//            Timber.plant(ReleaseTree())
        }
    }


    class XAccessKakaoInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", "KakaoAK d1f4a1d0fde030076b6e307bd925ac90")
                    .build()
            )
        }
    }

    class XAccessTokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjIsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDk5NjA1NCwiZXhwIjoxNzU4MTk2MDU0fQ.hQwGE6cYN3Y3nhipe93kbIGerm6VfgVAJI4T9K8lAaw")
                    .build()
            )
        }
    }

    private fun initRetrofitInstance() {

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        val clientKakao: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessKakaoInterceptor()) // JWT 자동 헤더 전송
            .build()




        // sRetrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitKakao = Retrofit.Builder()
            .baseUrl(KAKAO_LOCAL_API_URL)
            .client(clientKakao)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
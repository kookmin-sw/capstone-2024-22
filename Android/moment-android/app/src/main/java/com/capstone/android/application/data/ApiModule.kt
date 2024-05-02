package com.capstone.android.application.data

import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.data.remote.auth.AuthRetrofitInterface
import com.capstone.android.application.data.remote.card.CardRetrofitInterface
import com.capstone.android.application.data.remote.kakao.KakaoRetrofitInterface
import com.capstone.android.application.data.remote.open_weather.OpenWeatherRetrofitInterface
import com.capstone.android.application.data.remote.trip.TripRetrofitInterface
import com.capstone.android.application.data.remote.tripfile.TripFileRetrofitInterface
import com.capstone.android.application.domain.response.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseOkHttpClient
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoOkHttpClient
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenWeatherOkHttpClient




    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseRetrofit
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRetrofit
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenWeatherRetrofit




    @KakaoOkHttpClient
    @Singleton
    @Provides
    fun provideKakaoOkHttpClient() =
        OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(ApplicationClass.XAccessKakaoInterceptor()) // JWT 자동 헤더 전송
            .build()


    @OpenWeatherOkHttpClient
    @Singleton
    @Provides
    fun provideOpenWeatherOkHttpClient() =
        OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @BaseOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(ApplicationClass.XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()


    @BaseRetrofit
    @Singleton
    @Provides
    fun provideBaseRetrofit(@BaseOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(ApplicationClass.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @KakaoRetrofit
    @Singleton
    @Provides
    fun provideKakaoRetrofit(@KakaoOkHttpClient okHttpClient : OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApplicationClass.KAKAO_LOCAL_API_URL)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @OpenWeatherRetrofit
    @Singleton
    @Provides
    fun provideOpenWeatherRetrofit(@OpenWeatherOkHttpClient okHttpClient : OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApplicationClass.OPEN_WATHER_API_URL)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideTripService(@BaseRetrofit retrofit: Retrofit): TripRetrofitInterface {
        return retrofit.create(TripRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthService(@BaseRetrofit retrofit: Retrofit): AuthRetrofitInterface {
        return retrofit.create(AuthRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideCardService(@BaseRetrofit retrofit:Retrofit) : CardRetrofitInterface{
        return retrofit.create(CardRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideTripFileService(@BaseRetrofit retrofit:Retrofit) : TripFileRetrofitInterface{
        return retrofit.create(TripFileRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideKakaoService(@KakaoRetrofit retrofit:Retrofit) : KakaoRetrofitInterface {
        return retrofit.create(KakaoRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideOpenWeatherService(@OpenWeatherRetrofit retrofit:Retrofit) : OpenWeatherRetrofitInterface {
        return retrofit.create(OpenWeatherRetrofitInterface::class.java)
    }





//    @Singleton
//    @Provides
//    fun provideTripRepository(tripRetrofitInterface: TripRetrofitInterface) : TripRepository {
//        val test = TripRepository(tripRetrofitInterface)
//        return test
//    }

}

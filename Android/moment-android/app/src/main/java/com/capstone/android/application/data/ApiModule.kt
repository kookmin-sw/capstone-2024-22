package com.capstone.android.application.data

import com.capstone.android.application.app.ApplicationClass
import com.capstone.android.application.data.remote.auth.AuthRetrofitInterface
import com.capstone.android.application.data.remote.card.CardRetrofitInterface
import com.capstone.android.application.data.remote.receipt.ReceiptRetrofitInterface
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
    annotation class BaseRetrofit



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
    fun provideBaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(ApplicationClass.API_URL)
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
    fun provideReceiptService(@BaseRetrofit retrofit:Retrofit) : ReceiptRetrofitInterface {
        return retrofit.create(ReceiptRetrofitInterface::class.java)
    }



//    @Singleton
//    @Provides
//    fun provideTripRepository(tripRetrofitInterface: TripRetrofitInterface) : TripRepository {
//        val test = TripRepository(tripRetrofitInterface)
//        return test
//    }

}

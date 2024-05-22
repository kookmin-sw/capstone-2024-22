package com.capstone.android.application.domain.response

import android.util.Log
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotation: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        val wrapperType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(wrapperType) != ApiResponse::class.java) return null

        val bodyType = getParameterUpperBound(0, wrapperType as ParameterizedType)
        return ApiResponseCallAdapter<Any>(bodyType)
    }
}

private class ApiResponseCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Call<ApiResponse<T>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<ApiResponse<T>>
            = ApiResponseCall(call)
}

private class ApiResponseCall<T>(
    private val delegate: Call<T>
) : Call<ApiResponse<T>> {
    override fun enqueue(
        callback: Callback<ApiResponse<T>>
    ) = delegate.enqueue(
        object : Callback<T> {
            private fun Response<T>.toNetworkResponse(): ApiResponse<T> {
                return ApiResponse.Success(data = this.body()!!)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ApiResponseCall,
                    Response.success(response.toNetworkResponse())
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(
                    this@ApiResponseCall,
                    Response.success(ApiResponse.Error(throwable as Exception))
                )
            }
        }
    )

    override fun clone(): Call<ApiResponse<T>> {
        Log.d("weagwgweag","clone")
        TODO("Not yet implemented")
    }

    override fun execute(): Response<ApiResponse<T>> {
        Log.d("weagwgweag","execute")

        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        Log.d("weagwgweag","isExecuted")
        TODO("Not yet implemented")
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        Log.d("weagwgweag","isCanceled")
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        Log.d("weagwgweag","request")
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}

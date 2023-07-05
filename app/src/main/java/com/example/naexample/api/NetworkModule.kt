package com.example.naexample.api

import com.example.naexample.BuildConfig
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val TIMEOUT = 20L
        const val AUTHORIZATION = "Authorization"
        const val API_KEY = "zBbNdN88FPt70awvOdlDgkA8isj2gNZjiOdlwdUvAxXuN2wjCQ2S2U8F"
    }

    private val okHttpClient = OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)

    private val interceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder()
            .header(AUTHORIZATION, API_KEY)
            .method(request.method, request.body)
            .build()
        chain.proceed(newRequest)
    }

    @Singleton
    @Provides
    fun createApiRetrofit(): Retrofit {
        okHttpClient.apply {
            addInterceptor(OkHttpProfilerInterceptor())
            addInterceptor(interceptor)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): Api = retrofit.create()
}
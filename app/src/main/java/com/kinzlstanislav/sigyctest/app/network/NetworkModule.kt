package com.kinzlstanislav.sigyctest.app.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kinzlstanislav.sigyctest.BuildConfig
import com.kinzlstanislav.sigyctest.Constants
import com.kinzlstanislav.sigyctest.core.helpers.SingleToArrayAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Factory
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Single
    fun retrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

    @Single
    fun okkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .connectTimeout(Constants.OKHTTP_NETWORK_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constants.OKHTTP_NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Single
    fun moshi(): Moshi = Moshi.Builder()
        .add(SingleToArrayAdapter.FACTORY)
        .build()
}
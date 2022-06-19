package com.devtides.stackoverflowquery.model

import com.devtides.stackoverflowquery.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StackOverflowService {
    private const val BASE_URL = "https://api.stackexchange.com/"

    private var api: StackOverflowApi? = null
    private fun getApi(): StackOverflowApi {
        if (api == null) {
            val okHttpClient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(logging)

            }

            api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build()
                .create(StackOverflowApi::class.java)

        }

        return api!!

    }

    fun callApi() = getApi()


}
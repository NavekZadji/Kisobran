package com.example.kisobran

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class KisobranRepository {
    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build()

    private val retrofit: KisobranAPIService = Retrofit.Builder().baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(KisobranAPIService::class.java)
    suspend fun fetchKisobranFromAPI(): Response<KisobranNadskup> {
        return retrofit.fetchKisobran(latitude = 46.28, longitude = 16.539999, hourly = "weathercode",start_date = "2022-12-27",end_date="2022-12-31" )
    }

}
package com.example.kisobran

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class KisobranRepository {
    private val retrofit: KisobranAPIService = Retrofit.Builder().baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KisobranAPIService::class.java)
    suspend fun fetchKisobranFromAPI(): Response<KisobranNadskup> {
        return retrofit.fetchKisobran(latitude = 46.28, longitude = 16.539999, hourly = "weathercode", timezone= "Europe%2FBerlin",start_date = "2022-12-27",end_date="2022-12-31" )
    }

}
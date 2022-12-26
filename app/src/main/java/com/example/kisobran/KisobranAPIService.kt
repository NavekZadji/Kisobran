package com.example.kisobran

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KisobranAPIService {

    @GET("/v1/forecast")
    suspend fun fetchKisobran(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String,
        @Query("timezone") timezone: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
        ): Response<KisobranNadskup>
}
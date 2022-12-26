package com.example.kisobran

import com.google.gson.annotations.SerializedName

data class KisobranNadskup(
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double,
    @SerializedName("generationtime_ms")
    var generationtime_ms: Double,
    @SerializedName("utc_offset_seconds")
    var utc_offset_seconds: Double,
    @SerializedName("timezone")
    var timezone: String,
    @SerializedName("timezone_abbreviation")
    var timezone_abbreviation: String,
    @SerializedName("elevation")
    var elevation: String,
    @SerializedName("hourly_units")
    var hourly_units: HourlyUnits,
    @SerializedName("hourly")
    var hourly: Hourly
    )

package com.example.kisobran

import com.google.gson.annotations.SerializedName


data class HourlyUnits(
    @SerializedName("time")
    var time: String,
    @SerializedName("weathercode")
    var weathercode: String,
)

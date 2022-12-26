package com.example.kisobran

import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("time")
    var time: Array<String>,
    @SerializedName("weathercode")
    var weathercode: Array<Int>
)

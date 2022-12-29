package com.example.kisobran

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Array<Double>
}
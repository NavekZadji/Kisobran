package com.example.kisobran

import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.jar.Manifest
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume

interface LocationTracker {
    suspend fun getCurrentLocation():Location?
}
package com.example.kisobran

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class DohvatDozvole : PermissionTracker {
    override suspend fun provjeriDozvole(): Boolean {

        /*
        // registerForActivityResult mi ne prepoznaje kao funkciju

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){}
        */

        ActivityCompat.requestPermissions(
            MainActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 505
        )

        ActivityCompat.requestPermissions(
            MainActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 506
        )

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            KisobranApplication.appContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            KisobranApplication.appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return hasAccessCoarseLocationPermission && hasAccessFineLocationPermission
    }
}
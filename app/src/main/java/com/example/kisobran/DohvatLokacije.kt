package com.example.kisobran


import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient

class DohvatLokacije:LocationTracker {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    // default postavljeni na Zagreb
    private var zemljopisnaSirina: Double = 46.28
    private var zemljopisnaDuzina: Double = 16.539999


    override suspend fun getCurrentLocation(): Array<Double>{
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

        if (hasAccessCoarseLocationPermission && hasAccessFineLocationPermission) {

            fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                if (location != null) {
                    zemljopisnaSirina = location.latitude
                    zemljopisnaDuzina = location.longitude
                }
                if (location == null) {
                    zemljopisnaSirina = 46.28
                    zemljopisnaDuzina = 16.539999
                }
            }
        } else {
            zemljopisnaSirina = 46.28
            zemljopisnaDuzina = 16.539999
        }

        return arrayOf(zemljopisnaSirina,zemljopisnaDuzina)
    }

}
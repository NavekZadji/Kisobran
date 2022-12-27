package com.example.kisobran

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DefaultLocationTracker(
    private val locationCLient: FusedLocationProviderClient,
    private val application: Application
        ): LocationTracker{

        override suspend fun getCurrentLocation(): Location? {
            val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                KisobranApplication.appContext,android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                KisobranApplication.appContext,android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            // 45:00  https://www.youtube.com/watch?v=eAbKK7JNxCE, treba jos osim provjere permissiona stvarno i pitati za lokaciju

            if(!hasAccessCoarseLocationPermission && !hasAccessCoarseLocationPermission){
                return null
            }
            return suspendCancellableCoroutine { cont ->
                locationClient.lastLocation.apply{
                    if(isComplete){
                        if(isSuccessful) {
                            cont.resume(result)
                        } else{
                            cont.resume(null)
                        }
                        return@suspendCancellableCoroutine
                    }
                    addOnSuccessListener {
                        cont.resume(it)
                    }
                    addOnFailureListener{
                        cont.resume(null)
                    }
                    addOnCanceledListener{
                        cont.cancel()
                    }
                }
            }

    }


}
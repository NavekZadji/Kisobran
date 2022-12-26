package com.example.kisobran

import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import java.util.jar.Manifest
import kotlin.coroutines.coroutineContext

interface Lokacija {
    suspend fun getLokacija():Location? {

       val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
           KisobranApplication.appContext,android.Manifest.permission.ACCESS_FINE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED

        // 45:00  https://www.youtube.com/watch?v=eAbKK7JNxCE, treba jos osim provjere permissiona stvarno i pitati za lokaciju

    }

}
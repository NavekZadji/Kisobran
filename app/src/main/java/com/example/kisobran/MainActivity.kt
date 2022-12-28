package com.example.kisobran

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.component.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    private val viewModel by viewModel<KisobranViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var ZemljopisnaSirina: Double = 0.0
    private var ZemljopisnaDuzina: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // napraviti check permission ovdje umjesto u DefaultLocationTracker, mozda tako mogu izbjeci dependancy injection

        fun getLastKnownLocation() {

            val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                application, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                KisobranApplication.appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if(!hasAccessCoarseLocationPermission && !hasAccessFineLocationPermission) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        ZemljopisnaSirina = location.latitude
                        ZemljopisnaDuzina = location.longitude
                    }
                    if (location == null) {
                        // default postavljeni na Zagreb
                        ZemljopisnaSirina = 46.28
                        ZemljopisnaDuzina = 16.539999
                    }
                }
            } else {
                ZemljopisnaSirina = 46.28
                ZemljopisnaDuzina = 16.539999
            }
        }

        lifecycleScope.launch{
            val ulazniPodatci = viewModel.getKisobran()
            val ulazPrimjer = ulazniPodatci.body()!!.timezone_abbreviation
            Log.v("ovdje",ulazPrimjer)
        }


    }


}
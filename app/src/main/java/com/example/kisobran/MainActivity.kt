package com.example.kisobran

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), KoinComponent {

    private val viewModel by viewModel<KisobranViewModel>()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var zemljopisnaSirina: Double = 46.28
    private var zemljopisnaDuzina: Double = 16.539999

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    val vrijemeSad = LocalDateTime.now().format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun getLastKnownLocation() {

            val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                application, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                KisobranApplication.appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasAccessCoarseLocationPermission && !hasAccessFineLocationPermission) {
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        zemljopisnaSirina = location.latitude
                        zemljopisnaDuzina = location.longitude
                    }
                    if (location == null) {
                        // default postavljeni na Zagreb
                        zemljopisnaSirina = 46.28
                        zemljopisnaDuzina = 16.539999
                    }
                }
            } else {
                zemljopisnaSirina = 46.28
                zemljopisnaDuzina = 16.539999
            }
        }

        lifecycleScope.launch {
            getLastKnownLocation()
            // ne radi nista?!

            val ulazniPodatci = viewModel.getKisobran(
                geografskaSirina = zemljopisnaSirina,
                geografskaDuzina = zemljopisnaDuzina
            )
            val ulazniPrimjer = ulazniPodatci.body()?.timezone_abbreviation
            val weathercodeNiz = ulazniPodatci.body()?.hourly?.weathercode
            val timeNiz = ulazniPodatci.body()?.hourly?.time
            var prviKisovitiIndex = 0

            while(prviKisovitiIndex < weathercodeNiz!!.size){
                if(weathercodeNiz[prviKisovitiIndex] == 51 || weathercodeNiz[prviKisovitiIndex] == 53 || weathercodeNiz[prviKisovitiIndex] == 55 || weathercodeNiz[prviKisovitiIndex] == 56 || weathercodeNiz[prviKisovitiIndex] == 57 || weathercodeNiz[prviKisovitiIndex] == 61 || weathercodeNiz[prviKisovitiIndex] == 63 || weathercodeNiz[prviKisovitiIndex] == 65 || weathercodeNiz[prviKisovitiIndex] == 66 || weathercodeNiz[prviKisovitiIndex] == 67 || weathercodeNiz[prviKisovitiIndex] == 80 || weathercodeNiz[prviKisovitiIndex] == 81 || weathercodeNiz[prviKisovitiIndex] == 82 || weathercodeNiz[prviKisovitiIndex] == 95 || weathercodeNiz[prviKisovitiIndex] == 96 || weathercodeNiz[prviKisovitiIndex] == 99){
                    break
                }
                prviKisovitiIndex++
            }

            val prvoKisovitoVrijeme = timeNiz!![prviKisovitiIndex]

            Log.v("ovdje", ulazniPrimjer!!)
            Log.v("vrijeme", vrijemeSad)
            Log.v("prvi kisoviti sat", prvoKisovitoVrijeme)

        }


    }


}
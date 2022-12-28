package com.example.kisobran

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.kisobran.databinding.ActivityMainBinding
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity(), KoinComponent {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<KisobranViewModel>()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    // default postavljeni na Zagreb
    private var zemljopisnaSirina: Double = 46.28
    private var zemljopisnaDuzina: Double = 16.539999

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    val vrijemeSad = LocalDateTime.now().format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    val vrijemeSad2 = LocalDateTime.parse(vrijemeSad, formatter)

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
        }

        lifecycleScope.launch {
            getLastKnownLocation()
            // ne radi nista?!

            val ulazniPodatci = viewModel.getKisobran(
                geografskaSirina = zemljopisnaSirina,
                geografskaDuzina = zemljopisnaDuzina
            )
            val weathercodeNiz = ulazniPodatci.body()?.hourly?.weathercode
            val timeNiz = ulazniPodatci.body()?.hourly?.time
            var prviKisovitiIndex = 0

            while (prviKisovitiIndex < weathercodeNiz!!.size) {
                if (weathercodeNiz[prviKisovitiIndex] == 51 || weathercodeNiz[prviKisovitiIndex] == 53 || weathercodeNiz[prviKisovitiIndex] == 55 || weathercodeNiz[prviKisovitiIndex] == 56 || weathercodeNiz[prviKisovitiIndex] == 57 || weathercodeNiz[prviKisovitiIndex] == 61 || weathercodeNiz[prviKisovitiIndex] == 63 || weathercodeNiz[prviKisovitiIndex] == 65 || weathercodeNiz[prviKisovitiIndex] == 66 || weathercodeNiz[prviKisovitiIndex] == 67 || weathercodeNiz[prviKisovitiIndex] == 80 || weathercodeNiz[prviKisovitiIndex] == 81 || weathercodeNiz[prviKisovitiIndex] == 82 || weathercodeNiz[prviKisovitiIndex] == 95 || weathercodeNiz[prviKisovitiIndex] == 96 || weathercodeNiz[prviKisovitiIndex] == 99) {
                    break
                }
                prviKisovitiIndex++
            }

            var susa = 0

            if (prviKisovitiIndex == weathercodeNiz.size) {
                susa = 1
            }

            if (susa == 0) {
                val prvoKisovitoVrijeme = timeNiz!![prviKisovitiIndex]


                val vrijemeKise = LocalDateTime.parse(prvoKisovitoVrijeme, formatter)

                val daniDoKise: Long = ChronoUnit.DAYS.between(vrijemeSad2, vrijemeKise)
                val satiDoKise: Long = ChronoUnit.HOURS.between(vrijemeSad2, vrijemeKise) % 24

                val porukaIspisa: String =
                    "Prva kisa ce biti ${vrijemeKise.dayOfMonth}.${vrijemeKise.monthValue}.${vrijemeKise.year} u " +
                            "${vrijemeKise.hour} sati , sto je za $daniDoKise dana i $satiDoKise sati"

                porukaFinal.text = porukaIspisa
            }

            if (susa == 1) {
                val porukaIspisa = "Nece biti kise u dogledno vrijeme."
                porukaFinal.text = porukaIspisa
            }

        }
    }
}
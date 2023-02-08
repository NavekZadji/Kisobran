package com.example.kisobran

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.kisobran.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.component.KoinComponent
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity(), KoinComponent {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<KisobranViewModel>()

    // default postavljeni na Zagreb
    private var zemljopisnaSirina: Double = 46.28
    private var zemljopisnaDuzina: Double = 16.539999

    private var dohvatLok: DohvatLokacije? = null
    private var dozvole: DohvatDozvole? = null

    private var precizno = 0
    private var grubo = 0

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

        lifecycleScope.launch {

            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        false
                    ) -> {
                        // Precise location access granted.
                        precizno = 1
                    }
                    permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        false
                    ) -> {
                        // Only approximate location access granted.
                        grubo = 1
                    }
                    else -> {
                        // No location access granted.
                    }
                }
            }

            locationPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            Log.i("prije ifa", precizno.toString())

            ///   permissioni nisu aktivni tak dugo dok ne kliknem, a ako kliknem ne dogodi se vise nist

            if (precizno == 1 && grubo == 1) {
                zemljopisnaSirina = dohvatLok?.getCurrentLocation()!![0]
                zemljopisnaDuzina = dohvatLok?.getCurrentLocation()!![1]

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
}
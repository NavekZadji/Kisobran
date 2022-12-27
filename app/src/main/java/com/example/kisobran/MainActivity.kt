package com.example.kisobran

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.component.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    private val viewModel by viewModel<KisobranViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var ZemljopisnaSirina : Double = 0.0
    private var ZemljopisnaDuzina : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // napraviti check permission ovdje umjesto u DefaultLocationTracker, mozda tako mogu izbjeci dependancy injection

    fun getLastKnownLocation(){
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location ->
            if(location != null){
                ZemljopisnaSirina = location.latitude
                ZemljopisnaDuzina = location.longitude
            }
        }
    }

    }



}
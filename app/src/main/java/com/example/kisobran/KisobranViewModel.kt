package com.example.kisobran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class KisobranViewModel(private val kisobranRepository: KisobranRepository) : ViewModel() {

    suspend fun getKisobran(geografskaSirina:Double,geografskaDuzina:Double): Response<KisobranNadskup> {
        return kisobranRepository.fetchKisobranFromAPI(geografskaSirina,geografskaDuzina)
    }
}
package com.example.kisobran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class KisobranViewModel(private val kisobranRepository: KisobranRepository) : ViewModel() {

    suspend fun getKisobran(): Response<KisobranNadskup> {
        return kisobranRepository.fetchKisobranFromAPI()
    }
}
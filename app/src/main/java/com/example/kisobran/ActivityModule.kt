package com.example.kisobran

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {
    viewModel {
        KisobranViewModel(kisobranRepository = get())
    }
}
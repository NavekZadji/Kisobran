package com.example.kisobran

import org.koin.dsl.module

val appModule = module {
    single { KisobranRepository() }
}
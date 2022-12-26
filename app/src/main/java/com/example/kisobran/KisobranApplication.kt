package com.example.kisobran

import android.app.Application
import android.content.Context
import org.koin.core.context.startKoin

class KisobranApplication: Application()  {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin{
            modules(appModule, activityModule)
        }
    }

    companion object {

        lateinit  var appContext: Context

    }
}
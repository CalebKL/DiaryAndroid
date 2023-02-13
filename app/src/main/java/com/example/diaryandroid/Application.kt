package com.example.diaryandroid

import timber.log.Timber
import timber.log.Timber.Forest.plant

class Application:android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}
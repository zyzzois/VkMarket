package com.example.vkmarket

import android.app.Application
import com.example.offers.di.OffersScreenDependenciesStore
import com.example.vkmarket.di.DaggerAppComponent

class App: Application() {
    private val appComponent by lazy {
        DaggerAppComponent.factory().create(
            this,
            this.applicationContext
        )
    }

    override fun onCreate() {
        super.onCreate()
        OffersScreenDependenciesStore.dependencies = appComponent
    }
}
package com.example.vkmarket.di

import android.app.Application
import android.content.Context
import com.example.offers.di.OffersModule
import com.example.offers.di.OffersScreenDependencies
import com.example.offers.domain.OffersRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(modules = [OffersModule::class, ])
interface AppComponent: OffersScreenDependencies {
    override val offersRepository: OffersRepository
    override val context: Context

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

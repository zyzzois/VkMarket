package com.example.offers.di

import com.example.offers.presentation.OffersViewModel
import dagger.Component
import javax.inject.Scope

@OffersScope
@Component(dependencies = [OffersScreenDependencies::class])
internal interface OffersComponent {
    fun getOffersViewModel(): OffersViewModel

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: OffersScreenDependencies): Builder
        fun build(): OffersComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class OffersScope


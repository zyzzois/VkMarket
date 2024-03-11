package com.example.offers.di

import android.content.Context
import com.example.offers.domain.OffersRepository
import kotlin.properties.Delegates.notNull

interface OffersScreenDependencies {
    val offersRepository: OffersRepository
    val context: Context
}

interface OffersScreenDependenciesProvider {
    val dependencies: OffersScreenDependencies
    companion object : OffersScreenDependenciesProvider by OffersScreenDependenciesStore
}

object OffersScreenDependenciesStore: OffersScreenDependenciesProvider {
    override var dependencies: OffersScreenDependencies by notNull()
}
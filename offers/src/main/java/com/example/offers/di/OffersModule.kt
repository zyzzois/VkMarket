package com.example.offers.di

import android.content.Context
import com.example.offers.data.OffersRepositoryImpl
import com.example.offers.data.remote.OffersApiService
import com.example.offers.domain.OffersRepository
import com.example.offers.util.NetworkConnectionManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface OffersModule {
    @Binds
    fun bindOffersRepository(
        offersRepositoryImpl: OffersRepositoryImpl
    ): OffersRepository

    companion object {
        private const val BASE_URL = "https://dummyjson.com"

        @Provides
        fun provideNetworkConnectionManager(
            context: Context
        ): NetworkConnectionManager {
            return NetworkConnectionManager(context)
        }

        @Provides
        fun buildClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideApiService(retrofit: Retrofit): OffersApiService {
            return retrofit.create(OffersApiService::class.java)
        }
    }
}
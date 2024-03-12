package com.example.offers.data.remote

import com.example.offers.data.OffersRepositoryImpl.Companion.PAGE_SIZE
import com.example.offers.data.remote.dto.OffersDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OffersApiService {
    @GET("products")
    suspend fun getAllOffers(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int = PAGE_SIZE
    ): Response<OffersDto>

    @GET("/products/category/{category}")
    suspend fun getOffersByCategory(
        @Path("category") category: String
    ): Response<OffersDto>

    @GET("products/search")
    suspend fun getOffersByQuery(
        @Query("q") query: String
    ): Response<OffersDto>
}
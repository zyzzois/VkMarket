package com.example.offers.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.core_data.models.OfferModel

interface OffersRepository {
    fun getOffersByCategory(category: String): LiveData<PagingData<OfferModel>>
    fun getOffersByQuery(query: String): LiveData<PagingData<OfferModel>>
    fun getAllOffers(): LiveData<PagingData<OfferModel>>
}
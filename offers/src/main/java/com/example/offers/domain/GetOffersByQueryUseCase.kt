package com.example.offers.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.core_data.models.OfferModel
import javax.inject.Inject

class GetOffersByQueryUseCase @Inject constructor(
    private val repository: OffersRepository
) {
    operator fun invoke(query: String): LiveData<PagingData<OfferModel>> {
        return repository.getOffersByQuery(query)
    }
}
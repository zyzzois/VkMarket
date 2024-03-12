package com.example.offers.presentation

import androidx.paging.PagingData
import com.example.core_data.models.OfferModel

sealed class State {
    data object Initial: State()
    data object Loading: State()
    data object Error: State()
    data class Content(val offersList: PagingData<OfferModel>): State()
}
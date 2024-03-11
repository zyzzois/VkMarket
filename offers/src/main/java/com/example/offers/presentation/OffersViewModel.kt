package com.example.offers.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core_data.models.OfferModel
import com.example.offers.domain.GetAllOffersUseCase
import com.example.offers.domain.GetOffersByCategoryUseCase
import com.example.offers.domain.GetOffersByQueryUseCase
import com.example.offers.util.NetworkConnectionManager
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val getOffersByCategoryUseCase: GetOffersByCategoryUseCase,
    private val getOffersByQueryUseCase: GetOffersByQueryUseCase,
    private val getAllOffersUseCase: GetAllOffersUseCase,
    networkConnectionManager: NetworkConnectionManager
): ViewModel() {
    private val currentQuery = MutableLiveData<String?>(null)
    private val currentCategory = MutableLiveData<String?>(null)
    private val currentAllOffers: MutableLiveData<Unit?> = MutableLiveData(Unit)

    val networkConnection = networkConnectionManager.isConnected.asLiveData()

    var currentOffers = MediatorLiveData<PagingData<OfferModel>>().apply {
        addSource(currentQuery) { query ->
            if (!query.isNullOrEmpty()) {
                getOffersByQueryUseCase(query).cachedIn(viewModelScope).observeForever {
                    this.value = it
                }
            }
        }
        addSource(currentCategory) { category ->
            if (category != null) {
                getOffersByCategoryUseCase(category).cachedIn(viewModelScope).observeForever {
                    this.value = it
                }
            }
        }
        addSource(currentAllOffers) {
            if (it != null) {
                getAllOffersUseCase().cachedIn(viewModelScope).observeForever {
                    this.value = it
                }
            }
        }
    }

    fun updateOffersWithCategory(category: String?) {
        currentCategory.value = category
        currentQuery.value = null
        currentAllOffers.value = null
    }

    fun updateOffersWithQuery(query: String) {
        currentQuery.value = query
        currentCategory.value = null
        currentAllOffers.value = null
    }

    fun updateOffersWithAllOffers() {
        currentCategory.value = null
        currentQuery.value = null
        currentAllOffers.value = Unit
    }
}
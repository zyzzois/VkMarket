package com.example.offers.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.core_data.models.OfferModel
import com.example.core_ui.NavigationRoutes
import com.example.offers.domain.GetAllOffersUseCase
import com.example.offers.domain.GetOffersByCategoryUseCase
import com.example.offers.domain.GetOffersByQueryUseCase
import com.example.offers.util.NetworkConnectionManager
import com.google.gson.Gson
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

    private val networkConnection: LiveData<Boolean> = networkConnectionManager.isConnected.asLiveData()

    private var _state = MediatorLiveData<State>(State.Initial)
    val state: LiveData<State> get() = _state

    init {
        _state.apply {
            addSource(currentQuery) { query ->
                if (!query.isNullOrEmpty()) {
                    getOffersByQueryUseCase(query).cachedIn(viewModelScope).observeForever {
                        this.value = State.Content(it)
                    }
                }
            }
            addSource(networkConnection) { connected ->
                if (connected) {
                    updateOffersWithAllOffers()
                } else {
                    this.value = State.Error
                }
            }
            addSource(currentCategory) { category ->
                if (category != null) {
                    getOffersByCategoryUseCase(category).cachedIn(viewModelScope).observeForever {
                        this.value = State.Content(it)
                    }
                }
            }
            addSource(currentAllOffers) {
                if (it != null) {
                    getAllOffersUseCase().cachedIn(viewModelScope).observeForever {
                        this.value = State.Content(it)
                    }
                }
            }
        }
    }

    fun updateOffersWithCategory(category: String?) {
        _state.value = State.Loading
        currentCategory.value = category
        currentQuery.value = null
        currentAllOffers.value = null
    }

    fun updateOffersWithQuery(query: String?) {
        _state.value = State.Loading
        currentQuery.value = query ?: EMPTY_QUERY
        currentCategory.value = null
        currentAllOffers.value = null
    }

    fun updateOffersWithAllOffers() {
        _state.value = State.Loading
        currentCategory.value = null
        currentQuery.value = null
        currentAllOffers.value = Unit
    }

    // сейчас через диплинки можно передать только строку, поэтому так костыльно
    // https://stackoverflow.com/questions/65280719/android-navigation-component-passing-argument-with-safeargs-and-deep-link
    fun getUriFromModel(offer: OfferModel): Uri {
        val gson = Gson()
        val offerAsString = gson.toJson(offer)
        return Uri.parse("${NavigationRoutes.OFFER_DETAILS_SCREEN}?offer=$offerAsString")
    }

    companion object {
        const val EMPTY_QUERY = ""
    }
}
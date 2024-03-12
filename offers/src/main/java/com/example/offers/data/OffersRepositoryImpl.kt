package com.example.offers.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core_data.models.OfferModel
import com.example.offers.data.mapper.ModelsMapper
import com.example.offers.data.remote.BaseApiResponse
import com.example.offers.data.remote.OffersApiService
import com.example.offers.domain.OffersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val apiService: OffersApiService,
    private val mapper: ModelsMapper
): OffersRepository, BaseApiResponse() {
    override fun getOffersByCategory(
        category: String
    ): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { _ ->
            getOffersByCategoryPage(category)
        }
        return createPagerWithLoader(loader)
    }

    override fun getOffersByQuery(query: String): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { _ ->
            getOffersByQueryPage(query)
        }
        return createPagerWithLoader(loader)
    }

    override fun getAllOffers(): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { pageIndex ->
            getAllOffersPage(pageIndex)
        }
        return createPagerWithLoader(loader)
    }

    private fun createPagerWithLoader(loader: OffersPageLoader): LiveData<PagingData<OfferModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                OffersPageSource(loader)
            }
        ).liveData
    }

    private suspend fun getOffersByCategoryPage(
        category: String?
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = if (category == null) {
            apiService.getAllOffers(PAGE_SIZE).body()?.products
        } else {
            apiService.getOffersByCategory(category).body()?.products
        }
        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }

    private suspend fun getAllOffersPage(
        pageIndex: Int
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = apiService.getAllOffers(PAGE_SIZE*pageIndex).body()?.products

        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }

    private suspend fun getOffersByQueryPage(
        query: String
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = apiService.getOffersByQuery(query).body()?.products
        delay(500)
        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 2
    }
}
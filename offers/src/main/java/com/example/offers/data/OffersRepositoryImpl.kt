package com.example.offers.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core_data.models.OfferModel
import com.example.offers.data.mapper.ModelsMapper
import com.example.offers.data.remote.OffersApiService
import com.example.offers.domain.OffersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val apiService: OffersApiService,
    private val mapper: ModelsMapper
): OffersRepository {
    override fun getOffersByCategory(
        category: String
    ): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { _, pageSize ->
            getOffersByCategoryPage(pageSize, category)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                OffersPageSource(
                    loader = loader,
                    pageSize = 20
                )
            }
        ).liveData
    }

    override fun getOffersByQuery(query: String): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { _, _ ->
            getOffersByQueryPage(query)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                OffersPageSource(
                    loader = loader,
                    pageSize = 20
                )
            }
        ).liveData
    }

    override fun getAllOffers(): LiveData<PagingData<OfferModel>> {
        val loader: OffersPageLoader = { pageIndex, pageSize ->
            getAllOffersPage(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                OffersPageSource(
                    loader = loader,
                    pageSize = 20
                )
            }
        ).liveData
    }

    private suspend fun getOffersByCategoryPage(
        pageSize: Int,
        category: String?
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = if (category == null) {
            apiService.getAllOffers(pageSize).body()?.products
        } else {
            apiService.getOffersByCategory(category).body()?.products
        }
        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }

    private suspend fun getAllOffersPage(
        pageIndex: Int,
        pageSize: Int
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = apiService.getAllOffers(pageSize*pageIndex).body()?.products

        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }

    private suspend fun getOffersByQueryPage(
        query: String
    ): List<OfferModel> = withContext(Dispatchers.IO) {
        val list = apiService.getOffersByQuery(query).body()?.products
        delay(1000)
        list?.map {
            mapper.mapDtoToUiModel(it)
        } ?: emptyList()
    }
}
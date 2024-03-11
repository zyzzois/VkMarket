package com.example.offers.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core_data.models.OfferModel

typealias OffersPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<OfferModel>

class OffersPageSource (
    private val loader: OffersPageLoader,
    private val pageSize: Int
): PagingSource<Int, OfferModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OfferModel> {
        val page = params.key ?: 0
        return try {
            val offers = loader.invoke(page, params.loadSize)

            return LoadResult.Page(
                data = offers,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (offers.size == params.loadSize) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OfferModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(20) ?: page.nextKey?.minus(20)
    }
}



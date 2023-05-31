package org.obesifix.obesifix.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.obesifix.obesifix.network.ApiService
import org.obesifix.obesifix.network.FoodListItem

class RecommendationPagingSource(private val apiService: ApiService,
                                 private val token: String,
                                 private val id: String): PagingSource<Int, FoodListItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, FoodListItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getRecommendationUser(token, id)

            PagingSource.LoadResult.Page(
                data = response.foodList.orEmpty().filterNotNull(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.foodList?.isNotEmpty() == true) null else position + 1
            )
        } catch (exception: Exception) {
            return PagingSource.LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FoodListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}
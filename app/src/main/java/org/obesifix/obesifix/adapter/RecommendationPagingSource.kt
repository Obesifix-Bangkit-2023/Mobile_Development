package org.obesifix.obesifix.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.obesifix.obesifix.network.ApiService
import org.obesifix.obesifix.network.response.FoodListItem

class RecommendationPagingSource(private val apiService: ApiService,
                                 private val token: String,
                                 private val id: String,
                                 private val isLoading: MutableLiveData<Boolean>,
): PagingSource<Int, FoodListItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FoodListItem> {
        isLoading.postValue(true)
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getRecommendationUser("Bearer $token", id)

            LoadResult.Page(
                data = response.foodList.orEmpty(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.foodList?.isNotEmpty() == true) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }finally {
            isLoading.postValue(false)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FoodListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}
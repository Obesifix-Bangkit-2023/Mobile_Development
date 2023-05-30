//package org.obesifix.obesifix.adapter
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import org.obesifix.obesifix.network.ApiService
//
//class RecommendationPagingSource(private val apiService: ApiService): PagingSource<Int, RecommendationResponseItem>() {
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//
//    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, RecommendationResponseItem> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = apiService.getRecommendation(page, params.loadSize)
//
//            PagingSource.LoadResult.Page(
//                data = responseData,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
//            )
//        } catch (exception: Exception) {
//            return PagingSource.LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, RecommendationResponseItem>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}
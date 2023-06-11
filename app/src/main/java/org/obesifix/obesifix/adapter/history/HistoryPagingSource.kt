package org.obesifix.obesifix.adapter.history

import android.app.Application
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.obesifix.obesifix.adapter.RecommendationPagingSource
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.database.room.NutritionDao
import org.obesifix.obesifix.database.room.NutritionRoomDatabase

class HistoryPagingSource(private val application: Application, private val id:String, private val date:String): PagingSource<Int, HistoryNutrition>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryNutrition> {
        val nutritionDb: NutritionRoomDatabase = NutritionRoomDatabase.getDatabase(application)
        val nutritionDao: NutritionDao = nutritionDb.nutritionDao()
        return try {
            val nextPageNumber = params.key ?: INITIAL_PAGE_INDEX
            val pageSize = params.loadSize
            val offset = (nextPageNumber - 1) * pageSize

            val nutritionList = nutritionDao.getListNutritionByIdAndDate(id, date, pageSize, offset)

            LoadResult.Page(
                data = nutritionList,
                prevKey = if (nextPageNumber == INITIAL_PAGE_INDEX) null else nextPageNumber - 1,
                nextKey = if (nutritionList.size < pageSize) null else nextPageNumber + 1
            )
        } catch (exception: Exception) {
            Log.e("HR", "ADAPTER Exception: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HistoryNutrition>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
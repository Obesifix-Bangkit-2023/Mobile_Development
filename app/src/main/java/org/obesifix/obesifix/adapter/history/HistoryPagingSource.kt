package org.obesifix.obesifix.adapter.history

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.database.room.NutritionDao
import org.obesifix.obesifix.database.room.NutritionRoomDatabase

class HistoryPagingSource(application: Application, private val id:String, private val date:String): PagingSource<Int, HistoryNutrition>() {
    private var nutritionDao: NutritionDao?
    private var nutritionDb: NutritionRoomDatabase?

    init{
        nutritionDb = NutritionRoomDatabase.getDatabase(application)
        nutritionDao = nutritionDb?.nutritionDao()
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryNutrition> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val liveData = nutritionDao?.getListNutritionByIdAndDate(id, date)
            val response = liveData?.value
            val data = response ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isNotEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
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
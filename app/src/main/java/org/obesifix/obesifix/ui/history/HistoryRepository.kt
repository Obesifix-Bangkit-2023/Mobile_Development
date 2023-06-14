package org.obesifix.obesifix.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.obesifix.obesifix.adapter.history.HistoryPagingSource
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.database.room.NutritionDao
import org.obesifix.obesifix.database.room.NutritionRoomDatabase
import javax.inject.Inject

class HistoryRepository@Inject constructor(private val application: Application){
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var nutritionDao: NutritionDao?
    private var nutritionDb: NutritionRoomDatabase? = NutritionRoomDatabase.getDatabase(application)

    init{
        nutritionDao = nutritionDb?.nutritionDao()
    }

    fun getListNutritionByIdAndDate(id: String, date: String): LiveData<PagingData<HistoryNutrition>> {
        Log.d("HR", "getListNutritionByIdAndDate Inside history repository $id, $date")
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                HistoryPagingSource(application, id, date,_isLoading)
            }
        ).liveData
    }

    fun removeHistoryNutritionTodayById(id:Long){
        CoroutineScope(Dispatchers.IO).launch{
            nutritionDao?.removeHistoryNutritionTodayById(id)
        }
    }
}
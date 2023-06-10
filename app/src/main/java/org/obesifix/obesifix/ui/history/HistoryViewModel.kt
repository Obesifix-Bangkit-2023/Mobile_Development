package org.obesifix.obesifix.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.database.entity.HistoryNutrition
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel@Inject constructor(private val historyRepository: HistoryRepository): ViewModel() {
    val isLoading = historyRepository.isLoading

    fun getListNutritionByIdAndDate(id: String, date: String): LiveData<PagingData<HistoryNutrition>> =
        historyRepository.getListNutritionByIdAndDate(id,date).cachedIn(viewModelScope)

    fun removeHistoryNutritionTodayById(id:Long){
        historyRepository.removeHistoryNutritionTodayById(id)
    }
}
package org.obesifix.obesifix.ui.home.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.ui.home.HomeRepository
import javax.inject.Inject

@HiltViewModel
class ListViewModel@Inject constructor(private val listRepository: ListRepository): ViewModel() {
    val isLoading = listRepository.isLoading

    fun getRecommendation(token: String, id: String): LiveData<PagingData<FoodListItem>> =
        listRepository.getRecommendation(token, id).cachedIn(viewModelScope)

    fun getFilteredRecommendation(
        token: String,
        id: String,
        query: String
    ): LiveData<PagingData<FoodListItem>> {
        val recommendationLiveData = listRepository.getRecommendation(token, id)
        return recommendationLiveData.map { pagingData ->
            pagingData.filter { item ->
                item.name?.contains(query, ignoreCase = true) ?: false
            }
        }.cachedIn(viewModelScope)
    }

}
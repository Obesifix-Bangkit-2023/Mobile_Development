package org.obesifix.obesifix.ui.home.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import org.obesifix.obesifix.adapter.RecommendationPagingSource
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.FoodListItem

class ListRepository {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRecommendation(token: String, id:String): LiveData<PagingData<FoodListItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                RecommendationPagingSource(ApiConfig.getApiService(),token,id)
            }
        ).liveData
    }


}
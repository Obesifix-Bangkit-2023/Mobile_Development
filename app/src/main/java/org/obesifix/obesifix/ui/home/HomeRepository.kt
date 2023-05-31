package org.obesifix.obesifix.ui.home

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.DataUserResponse
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.network.RecommendationResponse
import org.obesifix.obesifix.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeRepository@Inject constructor(private val context: Context, private val pref: UserPreference) {

    private val _listItem = MutableLiveData<List<FoodListItem>?>()
    val listItem = _listItem

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getRecommendation(token:String, id:String){
        _isLoading.value = true
        try{
            val response = ApiConfig.getApiService().getRecommendationUser(token,id)
            val items = response.foodList
            if (items?.isNotEmpty() == true) {
                _listItem.value = response.foodList
            }

        }catch (e:Exception){
            Log.d("DETAIL","getSearchItem is error")
        }finally {
            _isLoading.value = false
        }
    }

}
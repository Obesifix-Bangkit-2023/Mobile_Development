package org.obesifix.obesifix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.obesifix.obesifix.ui.calculate.CalculateRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {
    val listItem = homeRepository.listItem
    val isLoading = homeRepository.isLoading

    fun getRecommendation(token:String, id:String){
        viewModelScope.launch {
            homeRepository.getRecommendation(token,id)
        }
    }

}
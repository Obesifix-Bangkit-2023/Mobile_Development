package org.obesifix.obesifix

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.obesifix.obesifix.network.FoodListItem

class SharedViewModel : ViewModel() {
    private val parcelData = MutableLiveData<FoodListItem>()

    fun setParcelData(data: FoodListItem) {
        parcelData.value = data
    }

    fun getParcelData(): LiveData<FoodListItem> {
        return parcelData
    }
}
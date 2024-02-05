package org.obesifix.obesifix

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.obesifix.obesifix.network.response.FoodListItem
import org.obesifix.obesifix.ui.calculate.SingleLiveEvent

class SharedViewModel : ViewModel() {
    private val parcelData = SingleLiveEvent<FoodListItem>()

    fun setParcelData(data: FoodListItem) {
        parcelData.value = data
    }

    fun getParcelData(): LiveData<FoodListItem> {
        return parcelData
    }
}
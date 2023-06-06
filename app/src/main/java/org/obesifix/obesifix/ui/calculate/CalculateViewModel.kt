package org.obesifix.obesifix.ui.calculate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.network.FoodListItem
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel@Inject constructor(private val calculateRepository: CalculateRepository) : ViewModel() {
    val isLoading = calculateRepository.isLoading
    val status = calculateRepository.status
    val userDataResponse = calculateRepository.userDataResponse

    private val nutritionData = MutableLiveData<NutritionData>()
    val nutritionLiveData: LiveData<NutritionData> = nutritionData

    init {
        calculateRepository.calCurrent.observeForever {
            updateNutritionData()
        }
        calculateRepository.calNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.fatCurrent.observeForever {
            updateNutritionData()
        }
        calculateRepository.fatNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.carbCurrent.observeForever {
            updateNutritionData()
        }
        calculateRepository.carbNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.proteinCurrent.observeForever {
            updateNutritionData()
        }
        calculateRepository.proteinNeed.observeForever {
            updateNutritionData()
        }
    }

    private fun updateNutritionData() {
        val data = NutritionData(
            calculateRepository.calCurrent.value,
            calculateRepository.calNeed.value,
            calculateRepository.fatCurrent.value,
            calculateRepository.fatNeed.value,
            calculateRepository.carbCurrent.value,
            calculateRepository.carbNeed.value,
            calculateRepository.proteinCurrent.value,
            calculateRepository.proteinNeed.value
        )
        nutritionData.value = data
    }

    fun getUserData(token:String, id:String){
        calculateRepository.getUserData(token,id)
    }

    fun addNutrition(data: FoodListItem){
        calculateRepository.addCalculation(data)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun triggerAlarmReset(){
        calculateRepository.scheduleDataReset()
    }

    data class NutritionData(
        val calCurrent: Float?,
        val calNeed: Float?,
        val fatCurrent: Float?,
        val fatNeed: Float?,
        val carbCurrent: Float?,
        val carbNeed: Float?,
        val proteinCurrent: Float?,
        val proteinNeed: Float?
    )
}
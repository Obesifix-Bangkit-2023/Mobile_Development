package org.obesifix.obesifix.ui.calculate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class NutritionData(
    val calCurrent: Float,
    val calNeed: Float,
    val fatCurrent: Float,
    val fatNeed: Float,
    val carbCurrent: Float,
    val carbNeed: Float,
    val proteinCurrent: Float,
    val proteinNeed: Float
)


@HiltViewModel
class CalculateViewModel@Inject constructor(private val calculateRepository: CalculateRepository) : ViewModel() {
    val isLoading = calculateRepository.isLoading
    val status = calculateRepository.status
    val userDataResponse = calculateRepository.userDataResponse

    private val nutritionDataLiveData = MutableLiveData<NutritionData>()
    private val nutritionData = NutritionData(
        calCurrent = calculateRepository.calCurrent.value ?: 0f,
        calNeed = calculateRepository.calNeed.value ?: 0f,
        fatCurrent = calculateRepository.fatCurrent.value ?: 0f,
        fatNeed = calculateRepository.fatNeed.value ?: 0f,
        carbCurrent = calculateRepository.carbCurrent.value ?: 0f,
        carbNeed = calculateRepository.carbNeed.value ?: 0f,
        proteinCurrent = calculateRepository.proteinCurrent.value ?: 0f,
        proteinNeed = calculateRepository.proteinNeed.value ?: 0f
    )

    fun getUserData(token:String, id:String){
        calculateRepository.getUserData(token,id)
    }

    fun getNutritionDataLiveData(): MutableLiveData<NutritionData> {
        nutritionDataLiveData.value = nutritionData
        return nutritionDataLiveData
    }



}
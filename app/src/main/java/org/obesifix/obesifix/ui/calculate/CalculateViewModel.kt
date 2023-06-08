package org.obesifix.obesifix.ui.calculate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.database.entity.NutritionSummary
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel@Inject constructor(private val calculateRepository: CalculateRepository) : ViewModel() {
    val isLoading = calculateRepository.isLoading
    val status = calculateRepository.status
    val userDataResponse = calculateRepository.userDataResponse

    private val nutritionData = MutableLiveData<NutritionData>()
    val nutritionLiveData: LiveData<NutritionData> = nutritionData

    init {
        calculateRepository.calNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.fatNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.carbNeed.observeForever {
            updateNutritionData()
        }
        calculateRepository.proteinNeed.observeForever {
            updateNutritionData()
        }
    }

    private fun updateNutritionData() {
        val data = NutritionData(
            calculateRepository.calNeed.value,
            calculateRepository.fatNeed.value,
            calculateRepository.carbNeed.value,
            calculateRepository.proteinNeed.value
        )
        nutritionData.value = data
    }

    fun getUserData(token:String, id:String){
        calculateRepository.getUserData(token,id)
    }

    data class NutritionData(
        val calNeed: Float?,
        val fatNeed: Float?,
        val carbNeed: Float?,
        val proteinNeed: Float?
    )

    private val _nutritionData = MutableLiveData<NutritionSummary>()
    val nutritionDataDb = _nutritionData

    fun getDataNutritionByIdAndDate(id:String, date:String){
        try{
            calculateRepository.getDataNutritionByIdAndDate(id,date)?.observeForever{ nutritionCurrent ->
                Log.d("FUN NC", "data nutritionCurrent $nutritionCurrent")
                _nutritionData.value = nutritionCurrent
                Log.d("FUN NC", "data nutritionCurrent ${_nutritionData.value}")
            }
        }catch (e:Exception){
            Log.d("getDataNutrition","getDataIsError")
        }
    }
}
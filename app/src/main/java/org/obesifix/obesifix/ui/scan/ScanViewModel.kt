package org.obesifix.obesifix.ui.scan

import androidx.lifecycle.ViewModel
import org.obesifix.obesifix.ui.detail.DetailRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel@Inject constructor(application: Application, private val detailRepository: DetailRepository) :
    AndroidViewModel(application) {

    fun addNutrition(userid: String, foodname: String, calorie: Float, fat: Float, protein: Float, carbohydrate: Float, date: String) {
        detailRepository.addNutritionData(userid, foodname, calorie, fat, protein, carbohydrate, date)
    }

}
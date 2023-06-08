package org.obesifix.obesifix.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(application: Application, private val detailRepository: DetailRepository) :
    AndroidViewModel(application) {

    fun addNutrition(userid: String, calorie: Float, fat: Float, protein: Float, carbohydrate: Float, date: String) {
        detailRepository.addNutritionData(userid, calorie, fat, protein, carbohydrate, date)
    }

}
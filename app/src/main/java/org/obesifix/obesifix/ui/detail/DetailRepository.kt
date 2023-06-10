package org.obesifix.obesifix.ui.detail

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.obesifix.obesifix.database.entity.Nutrition
import org.obesifix.obesifix.database.room.NutritionDao
import org.obesifix.obesifix.database.room.NutritionRoomDatabase
import javax.inject.Inject

class DetailRepository@Inject constructor(application: Application) {
    private var nutritionDao: NutritionDao?
    private var nutritionDb: NutritionRoomDatabase?

    init {
        nutritionDb = NutritionRoomDatabase.getDatabase(application)
        nutritionDao = nutritionDb?.nutritionDao()
    }

    fun addNutritionData(userid: String, foodname: String, calorie: Float, fat: Float, protein: Float, carbohydrate: Float, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val nutrition = Nutrition(userid = userid, foodname = foodname, calorie = calorie, fat = fat, protein = protein, carbohydrate = carbohydrate, date = date)
            nutritionDao?.addData(nutrition)
        }
    }


}
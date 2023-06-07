package org.obesifix.obesifix.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.obesifix.obesifix.database.entity.Nutrition
import org.obesifix.obesifix.database.entity.NutritionSummary
import java.util.*

@Dao
interface NutritionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(nutrition: Nutrition)

    @Query("SELECT SUM(nutrition.calorie) AS totalCalorie, " +
            "SUM(nutrition.fat) AS totalFat, SUM(nutrition.protein) AS totalProtein, " +
            "SUM(nutrition.carbohydrate) AS totalCarbohydrate FROM nutrition " +
            "WHERE nutrition.userid = :id AND nutrition.date = :date;")
    fun getNutritionByIdAndDate(id: String, date: Date): LiveData<NutritionSummary>

}
package org.obesifix.obesifix.database.entity

data class HistoryNutrition(
    val id:Long,
    val foodname: String,
    val calorie: Float,
    val fat: Float,
    val protein: Float,
    val carbohydrate: Float
)
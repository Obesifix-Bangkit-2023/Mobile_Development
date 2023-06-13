package org.obesifix.obesifix.network

data class FoodData(
	val name: String?,
	val serving: Int?,
	val total_cal: Double?,
	val total_fat: Double?,
	val total_protein: Double?,
	val total_carb: Double?,
	val description: String?
)

data class PredictionResponse(
	val status: Boolean,
	val statusCode: Int,
	val food_data: FoodData
)
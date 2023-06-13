package org.obesifix.obesifix.network

data class FoodData(
	val name: String?,
	val serving: Int?,
	val calorie: Double?,
	val fat: Double?,
	val protein: Double?,
	val carbohydrate: Double?,
	val description: String?
)

data class PredictionResponse(
	val status: Boolean,
	val statusCode: Int,
	val food_data: FoodData
)
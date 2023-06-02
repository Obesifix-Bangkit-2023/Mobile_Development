package org.obesifix.obesifix.network

data class PredictionResponse (
	val name: String?,
	val serving: Int?,
	val calorie: Double?,
	val fat: Double?,
	val protein: Double?,
	val carbohydrate: Double?,
	val description: String?
)
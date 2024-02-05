package org.obesifix.obesifix.network.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("food_data")
	val foodData: FoodData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class FoodData(

	@field:SerializedName("total_fat")
	val totalFat: Double? = null,

	@field:SerializedName("total_carb")
	val totalCarb: Double? = null,

	@field:SerializedName("total_protein")
	val totalProtein: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("serving")
	val serving: Int? = null,

	@field:SerializedName("total_cal")
	val totalCal: Double? = null
)

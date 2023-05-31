package org.obesifix.obesifix.network

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("food_data")
	val foodData: FoodData? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class FoodData(

	@field:SerializedName("protein")
	val protein: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("calorie")
	val calorie: Any? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Any? = null,

	@field:SerializedName("serving")
	val serving: Int? = null
)

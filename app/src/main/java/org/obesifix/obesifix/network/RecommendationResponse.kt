package org.obesifix.obesifix.network

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("food_list")
	val foodList: List<FoodListItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class FoodListItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("food_category")
	val foodCategory: String? = null,

	@field:SerializedName("protein")
	val protein: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("calorie")
	val calorie: Any? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("keyword")
	val keyword: String? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Any? = null
)

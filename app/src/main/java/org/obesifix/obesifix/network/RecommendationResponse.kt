package org.obesifix.obesifix.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecommendationResponse(

	@field:SerializedName("food_list")
	val foodList: List<FoodListItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

@Parcelize
data class FoodListItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("food_category")
	val foodCategory: String? = null,

	@field:SerializedName("protein")
	val protein: Float? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("calorie")
	val calorie: Float? = null,

	@field:SerializedName("fat")
	val fat: Float? = null,

	@field:SerializedName("keyword")
	val keyword: String? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Float? = null
) : Parcelable

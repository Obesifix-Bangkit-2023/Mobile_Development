package org.obesifix.obesifix.network

import com.google.gson.annotations.SerializedName

data class DataUserResponse(

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: UserData? = null,

)

data class UserData(
	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("height")
	val height: Float? = null,

	@field:SerializedName("weight")
	val weight: Float? = null,

	@field:SerializedName("activity")
	val activity: String? = null,

	@field:SerializedName("food_type")
	val foodType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,








)

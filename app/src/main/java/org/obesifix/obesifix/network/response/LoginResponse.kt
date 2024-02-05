package org.obesifix.obesifix.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val loginData: LoginData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class LoginData(

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)

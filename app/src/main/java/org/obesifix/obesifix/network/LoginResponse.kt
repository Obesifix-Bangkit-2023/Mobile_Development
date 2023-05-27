package org.obesifix.obesifix.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,


)

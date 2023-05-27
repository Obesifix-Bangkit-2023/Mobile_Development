package org.obesifix.obesifix.network

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,
)

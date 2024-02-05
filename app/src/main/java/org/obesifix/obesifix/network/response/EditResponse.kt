package org.obesifix.obesifix.network.response

import com.google.gson.annotations.SerializedName

data class EditResponse(
	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,


)

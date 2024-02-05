package org.obesifix.obesifix.network

import okhttp3.MultipartBody
import org.obesifix.obesifix.network.payload.EditBody
import org.obesifix.obesifix.network.payload.RegisterBody
import org.obesifix.obesifix.network.payload.LoginBody
import org.obesifix.obesifix.network.response.EditResponse
import org.obesifix.obesifix.network.response.LoginResponse
import org.obesifix.obesifix.network.response.PredictionResponse
import org.obesifix.obesifix.network.response.RecommendationResponse
import org.obesifix.obesifix.network.response.RegisterResponse
import org.obesifix.obesifix.network.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login/authjwt") //finish
    fun login(
        @Body loginBody: LoginBody
    ): Call<LoginResponse>

    @POST("register")   //finish
    fun register(
        @Body registerBody: RegisterBody
    ): Call<RegisterResponse>

    @GET("user/{userId}")  //finish
    fun getDataUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String? = null,
    ): Call<UserResponse>

    @PUT("user/{userId}") // finish
    fun editDataUser(
        @Header("Authorization") token: String,
        @Body editBody: EditBody,
        @Path("userId") userId: String? = null,
    ): Call<EditResponse>

    @Multipart
    @POST("prediction") // finish
    fun predictFood(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("recomendation/{userId}") //finish
    suspend fun getRecommendationUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String? = null,
    ): RecommendationResponse
}
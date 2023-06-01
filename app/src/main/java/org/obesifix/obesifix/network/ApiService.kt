package org.obesifix.obesifix.network

import okhttp3.MultipartBody
import org.obesifix.obesifix.network.body.RegisterBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("user/login")
    fun login(
        @Header("Authorization") token: String,
    ): Call<LoginResponse>

    @POST("user/register")
    fun register(
        @Header("Authorization") token: String,
        @Body registerBody: RegisterBody
    ): Call<RegisterResponse>

    @GET("user/{userId}")
    fun getDataUser(
        @Header("Authorization") token: String,
        @Query("userId") userId: String? = null,
    ): Call<DataUserResponse>

    @PUT("user/{userId}")
    fun editDataUser(
        @Header("Authorization") token: String,
        @Body registerBody: RegisterBody
    ): Call<EditResponse>

    @Multipart
    @POST("/prediction")
    fun predictFood(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("recomendation/{userId}")
    suspend fun getRecommendationUser(
        @Header("Authorization") token: String,
        @Query("userId") userId: String? = null,
    ): RecommendationResponse
}
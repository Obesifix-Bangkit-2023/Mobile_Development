package org.obesifix.obesifix.ui.scan

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.obesifix.obesifix.network.PredictionResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface Api {

    companion object {
        operator fun invoke(): Api {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(240, TimeUnit.SECONDS) // Adjust the timeout value as needed
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://complete-dot-test-obesifix-server.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api::class.java)
        }
    }

    @Multipart
    @POST("/prediction")
    fun predictFood(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}
package org.obesifix.obesifix.ui.scan

import okhttp3.MultipartBody
import org.obesifix.obesifix.network.PredictionResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    companion object {
        operator fun invoke(): Api {
            return Retrofit.Builder()
                .baseUrl("https://test-obesifix-server.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }

    @Multipart
    @POST("/prediction")
    fun predictFood(
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}
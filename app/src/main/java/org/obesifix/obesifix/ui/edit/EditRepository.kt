package org.obesifix.obesifix.ui.edit

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.response.EditResponse
import org.obesifix.obesifix.network.payload.EditBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class EditRepository@Inject constructor(private val context: Context){
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _editResponse = MutableLiveData<EditResponse>()
    val editResponse = _editResponse

    fun requestUpdateProfile(token: String, editBody: EditBody, userId:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().editDataUser("Bearer $token",editBody,userId)
        client.enqueue(object : Callback<EditResponse> {
            override fun onResponse(call: Call<EditResponse>, response: Response<EditResponse>) {
                _isLoading.value = false
                Log.d("REQ UPDATE","INSIDE")
                if(response.isSuccessful){
                    Log.d("REQ UPDATE","SUCCESS")
                    _editResponse.value = response.body()
                    Toast.makeText(context,  context.getString(R.string.success_update), Toast.LENGTH_SHORT ).show()
                } else {
                    if (response.code() == 403) {
                        // Handle 403 status code (Unauthorized/Session Expired)
                        Toast.makeText(context, "Response is success: ${response.message()}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Response is failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<EditResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context,  context.getString(R.string.failed_update), Toast.LENGTH_SHORT ).show()
                Log.d(ContentValues.TAG, "Request Login is Failed: ${t.message}")
            }
        })
    }
}
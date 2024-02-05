package org.obesifix.obesifix.ui.preference

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.payload.RegisterBody
import org.obesifix.obesifix.network.response.RegisterResponse
import org.obesifix.obesifix.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PreferenceRepository@Inject constructor(private val context: Context, private val pref: UserPreference){
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse = _registerResponse

    fun requestRegister( registerBody: RegisterBody){
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(registerBody)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.d("REQ REG","INSIDE")
                _isLoading.value = false
                if(response.isSuccessful){
                    Log.d("REQ REG","SUCCESS")
                    _registerResponse.value = response.body()
                    // Invoke the callback to navigate back
                    Toast.makeText(context, context.getString(R.string.success_update), Toast.LENGTH_SHORT ).show()
                }else{
                    Toast.makeText(context, context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                    Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context,  context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                Log.d(ContentValues.TAG, "Request Login is Failed: ${t.message}")
            }

        })
    }

}
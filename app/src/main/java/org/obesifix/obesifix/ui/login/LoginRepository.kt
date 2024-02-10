package org.obesifix.obesifix.ui.login

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.payload.LoginBody
import org.obesifix.obesifix.network.payload.RegisterBody
import org.obesifix.obesifix.network.response.LoginResponse
import org.obesifix.obesifix.network.response.RegisterResponse
import org.obesifix.obesifix.preference.UserModel
import org.obesifix.obesifix.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginRepository@Inject constructor(private val context: Context, private val pref: UserPreference) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse = _loginResponse

    fun saveUser(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.saveUser(user)
        }
    }

    fun requestLogin(loginBody:LoginBody){
        val client = ApiConfig.getApiService().login( loginBody)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("REQ LOGIN","INSIDE")
                if(response.isSuccessful){
                    Log.d("REQ LOGIN","SUCCESS")
                    _loginResponse.value = response.body()
                    val model = response.body()?.loginData?.run {
                        UserModel(
                            userId ?: "",
                            accessToken ?: "",
                            refreshToken ?: "",
                            true
                        )
                    }

                    if (model != null) {
                        saveUser(model)
                    }
                } else {
                    _loginResponse.value = LoginResponse(status = false)
                    Toast.makeText(context, "Response is failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Request is Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "Request is Failed: ${t.message}")
            }

        })
    }

}
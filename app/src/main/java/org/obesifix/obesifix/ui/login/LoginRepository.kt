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
import org.obesifix.obesifix.network.LoginResponse
import org.obesifix.obesifix.network.RegisterResponse
import org.obesifix.obesifix.network.body.RegisterBody
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

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse = _registerResponse

    fun saveUser(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.saveUser(user)
        }
    }

    fun requestRegister(token: String, registerBody: RegisterBody){
        _isLoading.value = true
        val client = ApiConfig.getApiService().register("Bearer $token", registerBody)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.d("REQ REG","INSIDE")
                _isLoading.value = false
                if(response.isSuccessful){
                    Log.d("REQ REG","SUCCESS")
                    _registerResponse.value = response.body()
                    val model = response.body()?.userId?.let {
                        UserModel(
                            it,
                            true
                        )
                    }
                    if (model != null) {
                        saveUser(model)
                    }
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

    fun requestLogin(token: String){
        val client = ApiConfig.getApiService().login("Bearer $token")
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("REQ LOGIN","INSIDE")
                if(response.isSuccessful){
                    Log.d("REQ LOGIN","SUCCESS")
                    _loginResponse.value = response.body()
                    val model = response.body()?.userId?.let {
                        UserModel(
                            it,
                            true
                        )
                    }
                    if (model != null) {
                        saveUser(model)
                    }
                } else {
                    _loginResponse.value = LoginResponse(false) // Set a default LoginResponse with status as false
                    Toast.makeText(context, context.getString(R.string.new_user), Toast.LENGTH_SHORT ).show()
                    Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context,  context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                Log.d(ContentValues.TAG, "Request Login is Failed: ${t.message}")
            }

        })
    }

}
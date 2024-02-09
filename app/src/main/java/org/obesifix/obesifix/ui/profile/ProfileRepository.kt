package org.obesifix.obesifix.ui.profile

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.payload.LoginBody
import org.obesifix.obesifix.network.response.LoginResponse
import org.obesifix.obesifix.network.response.LogoutResponse
import org.obesifix.obesifix.preference.UserModel
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository@Inject constructor(private val context: Context, private val pref: UserPreference) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> = _navigateToLogin

    fun requestLogout(token: String) {
        val client = ApiConfig.getApiService().logout(token = "Bearer $token")
        client.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    _navigateToLogin.value = response.body()?.statusCode == 200
                } else {
                    if (response.code() == 403) {
                        // Handle 403 status code (Unauthorized/Session Expired)
                        Toast.makeText(context, "Response is failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        _navigateToLogin.value = true
                    } else {
                        _navigateToLogin.value = false
                        Toast.makeText(context, "Response is failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                _navigateToLogin.value = false
                Toast.makeText(context, "Request Logout is Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "Request Logout is Failed: ${t.message}")
            }
        })
    }
}
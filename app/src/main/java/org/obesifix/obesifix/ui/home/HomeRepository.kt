package org.obesifix.obesifix.ui.home

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
import org.obesifix.obesifix.network.response.FoodListItem
import org.obesifix.obesifix.network.response.UserResponse
import org.obesifix.obesifix.preference.UserDataModel
import org.obesifix.obesifix.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeRepository@Inject constructor(private val context: Context, private val pref: UserPreference) {

    private val _listItem = MutableLiveData<List<FoodListItem>?>()
    val listItem = _listItem

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userDataResponse = MutableLiveData<UserResponse>()
    val userDataResponse = _userDataResponse

    suspend fun getRecommendation(token:String, id:String){
        _isLoading.value = true
        try{
            val response = ApiConfig.getApiService().getRecommendationUser("Bearer $token",id)
            val items = response.foodList
            if (items?.isNotEmpty() == true) {
                _listItem.value = response.foodList
            }

        }catch (e:Exception){
            Log.d("HOME","getRecommendation is error")
        }finally {
            _isLoading.value = false
        }
    }

    fun getUserData(token:String, id: String){
        _isLoading.value = true
        Log.d("token home repo", "token home repo: $token")
        val client = ApiConfig.getApiService().getDataUser("Bearer $token", id)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _userDataResponse.value = response.body()
                    val model = response.body()?.userData?.run {
                        UserDataModel(
                            userId = userId ?: "",
                            name = name ?: "",
                            email = email ?: "",
                            picture = picture ?: "",
                            age = age ?: 0,
                            gender = gender ?: "",
                            height = height ?: 0.0,
                            weight = weight ?: 0.0,
                            activity = activity ?: "",
                            foodType = foodType ?: "",
                            createdAt = createdAt ?: "",
                            updatedAt = updatedAt ?: ""
                        )
                    }
                    if (model != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            pref.saveUserData(model)
                        }
                    }
                    Toast.makeText(context, "Response is success ${response.message()}", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Response is failed ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "Response is failed ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, "Response is failed ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "Request get user data is Failed ${t.message}")
            }

        })
    }



}
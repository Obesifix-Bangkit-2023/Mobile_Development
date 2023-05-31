package org.obesifix.obesifix.ui.calculate

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.DataUserResponse
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.pow

class CalculateRepository@Inject constructor(private val context: Context, private val pref: UserPreference) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableLiveData<String>()
    val status = _status

    private val _calCurrent = MutableLiveData<Float>()
    val calCurrent = _calCurrent

    private val _calNeed = MutableLiveData<Float>()
    val calNeed = _calNeed

    private val _fatCurrent = MutableLiveData<Float>()
    val fatCurrent = _fatCurrent

    private val _fatNeed = MutableLiveData<Float>()
    val fatNeed = _fatNeed

    private val _carbCurrent = MutableLiveData<Float>()
    val carbCurrent = _carbCurrent

    private val _carbNeed = MutableLiveData<Float>()
    val carbNeed = _carbNeed

    private val _proteinCurrent = MutableLiveData<Float>()
    val proteinCurrent = _proteinCurrent

    private val _proteinNeed = MutableLiveData<Float>()
    val proteinNeed = _proteinNeed

    private val _userDataResponse = MutableLiveData<DataUserResponse>()
    val userDataResponse = _userDataResponse

    fun getUserData(token:String, id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDataUser("Bearer $token", id)
        client.enqueue(object : Callback<DataUserResponse> {
            override fun onResponse(call: Call<DataUserResponse>, response: Response<DataUserResponse>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _userDataResponse.value = response.body()
                    getUserStatus()
                    getCalNeed()
                    getCarbNeed()
                    getProteinNeed()
                    getFatNeed()
                }else{
                    Toast.makeText(context, context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                    Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DataUserResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context,  context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                Log.d(ContentValues.TAG, "Request get user data is Failed: ${t.message}")
            }

        })
    }

    fun getUserStatus(){
        val weight = _userDataResponse.value?.data?.weight
        val height = _userDataResponse.value?.data?.height
        val powHeight = height?.toDouble()?.pow(2.0)?.toFloat()

        val status = powHeight?.let { weight?.div(it) ?: 0f }

        if (status != null) {
            if(status < 18.5){
                _status.value = "Underweight"
            }else if(status in 18.5..24.9){
                _status.value = "Normal"
            }else if(status in 25.0..29.9){
                _status.value = "Overweight"
            }else if(status >= 30){
                _status.value = "Obese"
            }else{
                _status.value = "Cannot Recognized"
            }
        }
    }

    fun getCalNeed(){
        if(_status.value.equals("Underweight") || _status.value.equals("Normal")){
            //underweight and normal
            if(_userDataResponse.value?.data?.gender.equals("male")){
                //Male
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                var activityPoint: Float = 0.0f
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                if(activity.equals("sedentary")){
                    activityPoint = 1.00f
                }else if(activity.equals("low activity")){
                    activityPoint = 1.11f
                }else if(activity.equals("active")){
                    activityPoint = 1.25f
                }else if(activity.equals("very active")){
                    activityPoint = 1.48f
                }

                val eer = 662 - (9.35 * age!!) + activityPoint * (15.91 * weight!! + 539.6 * height!!)
                _calNeed.value = eer.toFloat()
            }else{
                //Female
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                var activityPoint: Float = 0.0f
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                if(activity.equals("sedentary")){
                    activityPoint = 1.00f
                }else if(activity.equals("low activity")){
                    activityPoint = 1.12f
                }else if(activity.equals("active")){
                    activityPoint = 1.27f
                }else if(activity.equals("very active")){
                    activityPoint = 1.45f
                }

                val eer = 354 - (6.91 * age!!) + activityPoint * (9.36 * weight!! + 726 * height!!)
                _calNeed.value = eer.toFloat()
            }

        }else{
            //overweight and obes person
            if(_userDataResponse.value?.data?.gender.equals("male")){
                //Male
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                var activityPoint: Float = 0.0f
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                if(activity.equals("sedentary")){
                    activityPoint = 1.00f
                }else if(activity.equals("low activity")){
                    activityPoint = 1.12f
                }else if(activity.equals("active")){
                    activityPoint = 1.29f
                }else if(activity.equals("very active")){
                    activityPoint = 1.59f
                }

                val eer = 1086 - (10.1 * age!!) + activityPoint * (13.7 * weight!! + 416 * height!!)
                _calNeed.value = eer.toFloat()
            }else{
                //Female
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                var activityPoint: Float = 0.0f
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                if(activity.equals("sedentary")){
                    activityPoint = 1.00f
                }else if(activity.equals("low activity")){
                    activityPoint = 1.16f
                }else if(activity.equals("active")){
                    activityPoint = 1.27f
                }else if(activity.equals("very active")){
                    activityPoint = 1.44f
                }

                val eer = 448 - (7.95 * age!!) + activityPoint * (11.4 * weight!! + 619 * height!!)
                _calNeed.value = eer.toFloat()
            }
        }
    }

    fun getCarbNeed() {
        val carbNeed = (60f / 100f) * (_calNeed.value ?: 0f) / 4f
        _carbNeed.value = carbNeed
    }

    fun getProteinNeed() {
        val proteinNeed = (20f / 100f) * (_calNeed.value ?: 0f) / 4f
        _proteinNeed.value = proteinNeed
    }

    fun getFatNeed() {
        val fatNeed = (20f / 100f) * (_calNeed.value ?: 0f) / 4f
        _fatNeed.value = fatNeed
    }

    fun addCalculation(data:FoodListItem){
        _calCurrent.value = _calCurrent.value?.plus(data.calorie!!)
        _fatCurrent.value = _fatCurrent.value?.plus(data.fat!!)
        _proteinCurrent.value = _proteinCurrent.value?.plus(data.fat!!)
        _carbCurrent.value = _carbCurrent.value?.plus(data.fat!!)
    }


}
package org.obesifix.obesifix.ui.calculate

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.obesifix.obesifix.R
import org.obesifix.obesifix.database.entity.NutritionSummary
import org.obesifix.obesifix.database.room.NutritionDao
import org.obesifix.obesifix.database.room.NutritionRoomDatabase
import org.obesifix.obesifix.network.ApiConfig
import org.obesifix.obesifix.network.DataUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.pow

class CalculateRepository@Inject constructor(private val context: Context, application: Application) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableLiveData<String>()
    val status = _status

    private val _calNeed = MutableLiveData<Float>()
    val calNeed = _calNeed

    private val _fatNeed = MutableLiveData<Float>()
    val fatNeed = _fatNeed

    private val _carbNeed = MutableLiveData<Float>()
    val carbNeed = _carbNeed

    private val _proteinNeed = MutableLiveData<Float>()
    val proteinNeed = _proteinNeed

    private val _userDataResponse = MutableLiveData<DataUserResponse>()
    val userDataResponse = _userDataResponse

    private var nutritionDao: NutritionDao?
    private var nutritionDb: NutritionRoomDatabase?

    init{
        nutritionDb = NutritionRoomDatabase.getDatabase(application)
        nutritionDao = nutritionDb?.nutritionDao()
    }

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
        Log.d(ContentValues.TAG, "getUserStatus ${_status.value}")
    }

    fun getCalNeed(){
        if(_status.value.equals("Underweight") || _status.value.equals("Normal")){
            //underweight and normal
            if(_userDataResponse.value?.data?.gender.equals("male")){
                //Male
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                val activityPoint = when (activity) {
                    "sedentary" -> 1.00f
                    "low activity" -> 1.11f
                    "active" -> 1.25f
                    "very active" -> 1.48f
                    else -> 0.0f
                }

                val eer = 662 - (9.35 * age!!) + activityPoint * (15.91 * weight!! + 539.6 * height!!)
                _calNeed.value = eer.toFloat()
                Log.d(ContentValues.TAG, "getCalNeed undernormal male${_calNeed.value}")
            }else{
                //Female
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                val activityPoint: Float = when (activity) {
                    "sedentary" -> 1.00f
                    "low activity" -> 1.12f
                    "active" -> 1.27f
                    "very active" -> 1.45f
                    else -> 0.0f
                }

                val eer = 354 - (6.91 * age!!) + activityPoint * (9.36 * weight!! + 726 * height!!)
                _calNeed.value = eer.toFloat()
                Log.d(ContentValues.TAG, "getCalNeed undernormal female${_calNeed.value}")
            }

        }else{
            //overweight and obes person
            if(_userDataResponse.value?.data?.gender.equals("male")){
                //Male
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                val activityPoint = when (activity) {
                    "sedentary" -> 1.00f
                    "low activity" -> 1.12f
                    "active" -> 1.29f
                    "very active" -> 1.59f
                    else -> 0.0f
                }

                val eer = 1086 - (10.1 * age!!) + activityPoint * (13.7 * weight!! + 416 * height!!)
                _calNeed.value = eer.toFloat()
                Log.d(ContentValues.TAG, "getCalNeed over male${_calNeed.value}")
            }else{
                //Female
                val age = _userDataResponse.value?.data?.age
                val activity = _userDataResponse.value?.data?.activity
                val weight = _userDataResponse.value?.data?.weight
                val height = _userDataResponse.value?.data?.height

                val activityPoint: Float = when (activity) {
                    "sedentary" -> 1.00f
                    "low activity" -> 1.16f
                    "active" -> 1.27f
                    "very active" -> 1.44f
                    else -> 0.0f
                }

                val eer = 448 - (7.95 * age!!) + activityPoint * (11.4 * weight!! + 619 * height!!)
                _calNeed.value = eer.toFloat()
                Log.d(ContentValues.TAG, "getCalNeed over female${_calNeed.value}")
            }
        }
    }

    fun getCarbNeed() {
        val carbNeed = (60f / 100f) * (_calNeed.value ?: 0f) / 4f
        _carbNeed.value = carbNeed
        Log.d(ContentValues.TAG, "getCarbNeed ${_carbNeed.value}")
        Log.d(ContentValues.TAG, "getCarbNeed var $carbNeed")
    }

    fun getProteinNeed() {
        val proteinNeed = (20f / 100f) * (_calNeed.value ?: 0f) / 4f
        _proteinNeed.value = proteinNeed
        Log.d(ContentValues.TAG, "getProteinNeed ${_proteinNeed.value}")
    }

    fun getFatNeed() {
        val fatNeed = (20f / 100f) * (_calNeed.value ?: 0f) / 4f
        _fatNeed.value = fatNeed
        Log.d(ContentValues.TAG, "getFatNeed ${_fatNeed.value}")
    }

    fun getDataNutritionByIdAndDate(id: String, date:String): LiveData<NutritionSummary>?{
        Log.d("FUN RepoNC", "INSIDE $id, $date")
        return nutritionDao?.getNutritionByIdAndDate(id,date)
    }
}
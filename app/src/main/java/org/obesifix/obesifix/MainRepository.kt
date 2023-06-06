package org.obesifix.obesifix

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import org.obesifix.obesifix.preference.UserModel
import org.obesifix.obesifix.preference.UserPreference
import javax.inject.Inject

class MainRepository@Inject constructor(private val context: Context, private val pref: UserPreference){
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}
package org.obesifix.obesifix

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.preference.UserModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(private val mainRepository: MainRepository): ViewModel(){
    fun getUser(): LiveData<UserModel> {
        return mainRepository.getUser()
    }


}
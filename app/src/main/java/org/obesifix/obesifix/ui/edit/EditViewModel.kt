package org.obesifix.obesifix.ui.edit

import androidx.lifecycle.ViewModel
import org.obesifix.obesifix.network.payload.EditBody
import javax.inject.Inject

class EditViewModel@Inject constructor(private val editRepository: EditRepository):ViewModel(){
    val isLoading = editRepository.isLoading
    val editResponse = editRepository.editResponse

    val isNavigate = editRepository.navigateToLogin
    fun requestUpdateProfile(token: String, editBody: EditBody, userId:String){
        editRepository.requestUpdateProfile(token, editBody,userId)
    }
}
package org.obesifix.obesifix.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.network.body.RegisterBody
import org.obesifix.obesifix.preference.UserPreference
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(private val loginRepository: LoginRepository):ViewModel() {

    val loginResponse = loginRepository.loginResponse
    val registerResponse = loginRepository.registerResponse

    fun requestLogin(token: String) =
        loginRepository.requestLogin(token)

    fun requestRegister(token: String, registerBody: RegisterBody) =
        loginRepository.requestRegister(token, registerBody)
}
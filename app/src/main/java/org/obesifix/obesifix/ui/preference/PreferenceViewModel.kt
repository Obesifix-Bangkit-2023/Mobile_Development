package org.obesifix.obesifix.ui.preference

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.network.payload.RegisterBody
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel@Inject constructor(private val preferenceRepository: PreferenceRepository) : ViewModel() {

    val preferenceResponse = preferenceRepository.registerResponse

    val isLoading = preferenceRepository.isLoading

    fun requestRegister(registerBody: RegisterBody) =
        preferenceRepository.requestRegister(registerBody)
}
package org.obesifix.obesifix.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.obesifix.obesifix.network.payload.LoginBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    val isLoading = profileRepository.isLoading

    val isNavigate = profileRepository.navigateToLogin

    fun requestLogout(token:String) =
        profileRepository.requestLogout(token)
}
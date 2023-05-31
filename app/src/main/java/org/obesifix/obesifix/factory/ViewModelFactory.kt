package org.obesifix.obesifix.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.obesifix.obesifix.di.Injection
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.calculate.CalculateViewModel
import org.obesifix.obesifix.ui.home.HomeViewModel
import org.obesifix.obesifix.ui.login.LoginViewModel


class ViewModelFactory(private val context: Context, private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.loginRepository(context, pref)) as T
            }
            modelClass.isAssignableFrom(CalculateViewModel::class.java) -> {
                CalculateViewModel(Injection.calculateRepository(context, pref)) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Injection.homeRepository(context, pref)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

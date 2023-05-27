package org.obesifix.obesifix.di

import android.content.Context
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginRepository

object Injection {
    fun loginRepository(context: Context, userPreference: UserPreference):LoginRepository{
        return LoginRepository(context,userPreference)
    }
}
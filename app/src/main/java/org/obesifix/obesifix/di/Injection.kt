package org.obesifix.obesifix.di

import android.content.Context
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.calculate.CalculateRepository
import org.obesifix.obesifix.ui.home.HomeRepository
import org.obesifix.obesifix.ui.home.list.ListRepository
import org.obesifix.obesifix.ui.login.LoginRepository

object Injection {
    fun loginRepository(context: Context, userPreference: UserPreference):LoginRepository{
        return LoginRepository(context,userPreference)
    }

    fun calculateRepository(context: Context, userPreference: UserPreference):CalculateRepository{
        return CalculateRepository(context,userPreference)
    }

    fun homeRepository(context: Context, userPreference: UserPreference): HomeRepository {
        return HomeRepository(context,userPreference)
    }

    fun listRepository(): ListRepository {
        return ListRepository()
    }
}
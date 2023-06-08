package org.obesifix.obesifix.di

import android.app.Application
import android.content.Context
import org.obesifix.obesifix.MainRepository
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.calculate.CalculateRepository
import org.obesifix.obesifix.ui.detail.DetailRepository
import org.obesifix.obesifix.ui.home.HomeRepository
import org.obesifix.obesifix.ui.home.list.ListRepository
import org.obesifix.obesifix.ui.login.LoginRepository

object Injection {
    fun loginRepository(context: Context, userPreference: UserPreference):LoginRepository{
        return LoginRepository(context,userPreference)
    }

    fun calculateRepository(context: Context, application: Application):CalculateRepository{
        return CalculateRepository(context,application)
    }

    fun homeRepository(context: Context, userPreference: UserPreference): HomeRepository {
        return HomeRepository(context,userPreference)
    }

    fun listRepository(): ListRepository {
        return ListRepository()
    }

    fun mainRepository(context: Context, userPreference: UserPreference): MainRepository {
        return MainRepository(context,userPreference)
    }

    fun detailRepository(application: Application): DetailRepository{
        return DetailRepository(application)
    }
}
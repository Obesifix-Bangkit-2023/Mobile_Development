package org.obesifix.obesifix.factory

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.obesifix.obesifix.MainViewModel
import org.obesifix.obesifix.di.Injection
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.calculate.CalculateViewModel
import org.obesifix.obesifix.ui.detail.DetailViewModel
import org.obesifix.obesifix.ui.edit.EditViewModel
import org.obesifix.obesifix.ui.history.HistoryViewModel
import org.obesifix.obesifix.ui.home.HomeViewModel
import org.obesifix.obesifix.ui.home.list.ListViewModel
import org.obesifix.obesifix.ui.login.LoginViewModel
import org.obesifix.obesifix.ui.scan.ScanViewModel


class ViewModelFactory(private val context: Context, private val pref: UserPreference, private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.loginRepository(context, pref)) as T
            }
            modelClass.isAssignableFrom(EditViewModel::class.java) -> {
                EditViewModel(Injection.editRepository(context)) as T
            }
            modelClass.isAssignableFrom(CalculateViewModel::class.java) -> {
                CalculateViewModel(Injection.calculateRepository(context, application)) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Injection.homeRepository(context, pref)) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(Injection.listRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.mainRepository(context, pref)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(application,Injection.detailRepository(application)) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(Injection.historyRepository(application)) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(application,Injection.detailRepository(application)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

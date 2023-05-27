package org.obesifix.obesifix.ui.preference

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityDetailBinding
import org.obesifix.obesifix.databinding.ActivityPreferenceBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginViewModel
import org.obesifix.obesifix.ui.login.dataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPreferenceBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(this,
                ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore))
            )[LoginViewModel::class.java]


    }
}
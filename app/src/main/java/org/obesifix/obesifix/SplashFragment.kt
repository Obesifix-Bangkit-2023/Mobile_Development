package org.obesifix.obesifix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.obesifix.obesifix.databinding.FragmentSplashBinding
import org.obesifix.obesifix.onboarding.ViewPagerFragment
import org.obesifix.obesifix.preference.UserPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SplashFragment : AppCompatActivity() {
    private lateinit var binding:FragmentSplashBinding
    private val delayMillis = 4000L
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userPreference = UserPreference.getInstance(this.dataStore)

        lifecycleScope.launch {
            val userIsLogin = userPreference.getUserLogin().first()
            Log.d("main userIsLogin", "userIsLogin $userIsLogin")
            Handler().postDelayed({
                if (!userIsLogin) {
                    startActivity(Intent(this@SplashFragment, ViewPagerFragment::class.java))
                } else {
                    startActivity(Intent(this@SplashFragment, MainActivity::class.java))
                }
                finish()
            }, delayMillis)
        }
    }
}
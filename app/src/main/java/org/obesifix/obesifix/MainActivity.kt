package org.obesifix.obesifix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.obesifix.obesifix.databinding.ActivityMainBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.response.FoodListItem
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // Hide the default action bar
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this, UserPreference.getInstance(dataStore), application)
        )[MainViewModel::class.java]
//        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        val navView: BottomNavigationView = binding.navView

        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        val parcelData: FoodListItem? = intent.getParcelableExtra("data")

//        just for moving to fragment calculate
        if (parcelData != null) {
//          sharedViewModel.setParcelData(parcelData)
            val desiredTabId = R.id.navigation_calculate
            navView.selectedItemId = desiredTabId
        }

        mainViewModel.getUser().observe(this) { user ->
            Log.d("main user","$user")
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

    }
}
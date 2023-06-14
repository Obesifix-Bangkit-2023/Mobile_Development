package org.obesifix.obesifix.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.obesifix.obesifix.adapter.LoadingStateAdapter
import org.obesifix.obesifix.adapter.history.HistoryAdapter
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.databinding.ActivityHistoryBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnRemoveClickListener {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth
    private val calendar = Calendar.getInstance()

    //current Date
    private val currentDate = calendar.time

    //selected Date formatted
    private var formattedDate: String = ""

    //format Date
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        historyViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    applicationContext,
                    UserPreference.getInstance(applicationContext.dataStore),
                    application
                )
            )[HistoryViewModel::class.java]
        adapter = HistoryAdapter(historyViewModel, this)
        adapter.onRemoveClickListener = this
        userPreference = UserPreference.getInstance(dataStore)
        auth = Firebase.auth
        setupAction()
    }

    private fun setupAction() {
        Log.d("setupAction", "Inside setupAction")
        val layoutManager = LinearLayoutManager(this)
        binding.rvListHistory.layoutManager = layoutManager
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        historyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.rvListHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        formattedDate = dateFormat.format(currentDate)
        val idFlow = userPreference.getUserId().toString()
        Log.d("id", "idFlow Inside setupAction")

        val user: FirebaseUser? = auth.currentUser
        var token: String?

        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result?.token
                    if (token != null) {
                        // Token retrieval successful
                        val idFlow: Flow<String> = userPreference.getUserId()
                        lifecycleScope.launchWhenStarted {
                            idFlow.collect { id ->
                                historyViewModel.getListNutritionByIdAndDate(id, formattedDate)
                                    .observe(this@HistoryActivity) {
                                        lifecycleScope.launch {
                                            adapter.submitData(it)
                                        }
                                    }
                            }
                        }
                    } else {
                        // Token is null
                        Log.d("TOKEN", "TOKEN IS NULL")
                    }
                } else {
                    // Task failed
                    Log.d("TOKEN", "FAILED TO CONNECT TO FIREBASE CLASS")
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onRemoveClicked(historyNutrition: HistoryNutrition) {
        historyViewModel.removeHistoryNutritionTodayById(historyNutrition.id)

    }
}
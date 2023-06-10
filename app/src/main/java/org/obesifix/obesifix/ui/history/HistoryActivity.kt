package org.obesifix.obesifix.ui.history

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.obesifix.obesifix.adapter.LoadingStateAdapter
import org.obesifix.obesifix.adapter.history.HistoryAdapter
import org.obesifix.obesifix.databinding.ActivityHistoryBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var token:String
    private lateinit var idFlow:String
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
            ViewModelProvider(this,
                    ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore), application)
                )[HistoryViewModel::class.java]
        adapter = HistoryAdapter(historyViewModel)
        setupAction()
    }

    private fun setupAction() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListHistory.layoutManager = layoutManager
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        historyViewModel.isLoading.observe(this){
            showLoading(it)
        }
        //not found
        binding.rvListHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        formattedDate = dateFormat.format(currentDate)
        idFlow = userPreference.getUserId().toString()
        historyViewModel.getListNutritionByIdAndDate(idFlow, formattedDate).observe(this@HistoryActivity){ pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
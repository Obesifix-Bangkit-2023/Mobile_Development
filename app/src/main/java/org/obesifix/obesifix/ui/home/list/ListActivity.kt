package org.obesifix.obesifix.ui.home.list

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.obesifix.obesifix.R
import org.obesifix.obesifix.adapter.LoadingStateAdapter
import org.obesifix.obesifix.adapter.RecommendationListAdapter
import org.obesifix.obesifix.databinding.ActivityListBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var listViewModel: ListViewModel
    private lateinit var adapter: RecommendationListAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = RecommendationListAdapter()
        listViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    applicationContext,
                    UserPreference.getInstance(applicationContext.dataStore),
                    application
                )
            )[ListViewModel::class.java]
        userPreference = UserPreference.getInstance(dataStore)
        supportActionBar?.hide()
        setupAction()
    }

    private fun setupAction() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListRecommendation.layoutManager = layoutManager

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.editTextSearch

        val closeButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_baseline_close_24)
        closeButton.setColorFilter(ContextCompat.getColor(this, R.color.white))

        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setImageResource(R.drawable.ic_baseline_search_24)

        val queryTextView = searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
        queryTextView.setHintTextColor(ContextCompat.getColor(this, R.color.white))
        queryTextView.setTextColor(ContextCompat.getColor(this, R.color.white))

        binding.editTextSearch.setOnClickListener {
            binding.editTextSearch.isIconified = false
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })

        listViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.rvListRecommendation.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        lifecycleScope.launch {
            val token: String = userPreference.getAccessTokenUser().first()
            val id: String = userPreference.getUserId().first()
            Log.d("token home", "token home $token")
            Log.d("id home", "id home $id")

            listViewModel.getRecommendation(token,id).observe(this@ListActivity){
                pagingData -> adapter.submitData(lifecycle, pagingData)
            }
        }


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun performSearch(query: String?) {
        // You can perform your search logic here and update the adapter with the filtered results
        // Make sure to use the appropriate method from your ViewModel to fetch the filtered data
        if (query != null) {
            lifecycleScope.launch {
                val token: String = userPreference.getAccessTokenUser().first()
                val id: String = userPreference.getUserId().first()
                Log.d("token home", "token home $token")
                Log.d("id home", "id home $id")

                listViewModel.getFilteredRecommendation(token, id, query)
                                    .observe(this@ListActivity) { pagingData ->
                                        adapter.submitData(lifecycle, pagingData)
                                    }
            }
        }
    }
}
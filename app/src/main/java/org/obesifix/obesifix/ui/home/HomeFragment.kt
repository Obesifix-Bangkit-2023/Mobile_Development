package org.obesifix.obesifix.ui.home

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import org.obesifix.obesifix.databinding.FragmentHomeBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.home.list.ListActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rvRecommendation: RecyclerView
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireContext().applicationContext as Application
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext(), UserPreference.getInstance(requireContext().dataStore), application)
        )[HomeViewModel::class.java]
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        auth = Firebase.auth

        binding.tvSeeAllRecommendation.setOnClickListener {
            val intent = Intent(requireContext(), ListActivity::class.java)
            startActivity(intent)
        }

        rvRecommendation = binding.innerRecyclerView
        rvRecommendation.setHasFixedSize(true)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvRecommendation.layoutManager = LinearLayoutManager(requireContext())
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.listItem.observe(viewLifecycleOwner){items->
            val adapter = items?.let { ListRecommendationAdapter(it) }
            binding.innerRecyclerView.adapter = adapter
        }

        val user: FirebaseUser? = auth.currentUser
        val profileImageUrl: String? = user?.photoUrl?.toString()
        profileImageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.profileImg)
        }

        val userName: String? = auth.currentUser?.displayName
        binding.tvName.text = userName

        var token: String? = null
        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result?.token
                    if (token != null) {
                        // Token retrieval successful
                        val idFlow: Flow<String> = userPreference.getUserId()
                        lifecycleScope.launchWhenStarted {
                            idFlow.collect { id ->
                                homeViewModel.getRecommendation(token!!, id)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
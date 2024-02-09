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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

        homeViewModel.listItem.observe(viewLifecycleOwner) { items ->
            val adapter = items?.let { ListRecommendationAdapter(it) }
            binding.innerRecyclerView.adapter = adapter
        }

        lifecycleScope.launch {
            val token: String = userPreference.getAccessTokenUser().first()
            val id: String = userPreference.getUserId().first()
            Log.d("token home", "token home $token")
            Log.d("id home", "id home $id")
            homeViewModel.getUserData(token, id)
            homeViewModel.userData.observe(viewLifecycleOwner) { user ->

                val profileImageUrl: String? = user?.userData?.picture
                profileImageUrl?.let {
                    Glide.with(requireContext())
                        .load(it)
                        .into(binding.profileImg)
                }

                binding.tvName.text = user?.userData?.name

                // Check if user data is available before calling getRecommendation
                if (user != null) {
                    homeViewModel.getRecommendation(token, id)
                }
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
package org.obesifix.obesifix.ui.calculate

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import org.obesifix.obesifix.R
import org.obesifix.obesifix.SharedViewModel
import org.obesifix.obesifix.databinding.FragmentCalculateBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class CalculateFragment : Fragment() {

    private lateinit var calculateViewModel: CalculateViewModel
    private var _binding: FragmentCalculateBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedViewModel: SharedViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calculateViewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext(), UserPreference.getInstance(requireContext().dataStore))
        )[CalculateViewModel::class.java]
        userPreference = UserPreference.getInstance(requireContext().dataStore)
        _binding = FragmentCalculateBinding.inflate(inflater, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        calculateViewModel.triggerAlarmReset()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalculateBinding.bind(view)
        auth = Firebase.auth
        setupAction()
        setupAdd()
    }

    private fun setupAdd() {
        sharedViewModel.getParcelData().observe(viewLifecycleOwner){ data ->
            Log.d("datasetupAdd", "ini setup $data")
            calculateViewModel.addNutrition(data)
        }
    }

    private fun setupAction() {
        val user: FirebaseUser? = auth.currentUser
        val userName: String? = auth.currentUser?.displayName
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
                                calculateViewModel.getUserData(token!!, id)
                                calculateViewModel.nutritionLiveData.observe(viewLifecycleOwner){ nutritionData ->
                                    binding.tvCalDesc.text = "${nutritionData.calCurrent} from ${nutritionData.calNeed} Kcal"
                                    binding.tvFatDesc.text = "${nutritionData.fatCurrent} from ${nutritionData.fatNeed} g"
                                    binding.tvProteinDesc.text = "${nutritionData.proteinCurrent} from ${nutritionData.proteinNeed} g"
                                    binding.tvCarboDesc.text = "${nutritionData.carbCurrent} from ${nutritionData.carbNeed} g"
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

        calculateViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        binding.tvNameDesc.text = "$userName"
        calculateViewModel.status.observe(viewLifecycleOwner){ status ->
            binding.tvStatusDesc.text = status
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        binding.addButton.setOnClickListener {
            val desiredTabId = R.id.navigation_scan
            bottomNavigationView.selectedItemId = desiredTabId
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}

package org.obesifix.obesifix.ui.calculate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import org.obesifix.obesifix.databinding.FragmentCalculateBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.detail.DetailActivity
import org.obesifix.obesifix.ui.scan.ScanFragment

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class CalculateFragment : Fragment() {

    private lateinit var calculateViewModel: CalculateViewModel
    private var _binding: FragmentCalculateBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth
    private var recommendationList: FoodListItem? = null

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
        arguments?.let {
            recommendationList = it.getParcelable(EXTRA_ID)!!
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalculateBinding.bind(view)
        auth = Firebase.auth
        setupAction()
        setupAddData()
    }

    private fun setupAddData() {
        recommendationList?.let {
            calculateViewModel.addNutrition(it)
        }
    }

    private fun setupAction() {
        val user: FirebaseUser? = auth.currentUser
        val userName: String? = auth.currentUser?.displayName
        var token: String? = null
        user?.getIdToken(false)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result?.token
                    if (token != null) {
                        // Token retrieval successful
                        val idFlow: Flow<String> = userPreference.getUserId()
                        lifecycleScope.launchWhenStarted {
                            idFlow.collect { id ->
                                calculateViewModel.getUserData(token!!, id)
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

        calculateViewModel.getNutritionDataLiveData().observe(viewLifecycleOwner){ nutritionData ->
            binding.tvCalDesc.text = "${nutritionData.calCurrent} from ${nutritionData.calNeed}"
            binding.tvFatDesc.text = "${nutritionData.fatCurrent} from ${nutritionData.fatNeed}"
            binding.tvProteinDesc.text = "${nutritionData.proteinCurrent} from ${nutritionData.proteinNeed}"
            binding.tvCarboDesc.text = "${nutritionData.carbCurrent} from ${nutritionData.carbNeed}"
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), ScanFragment::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}

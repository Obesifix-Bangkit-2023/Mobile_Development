package org.obesifix.obesifix.ui.profile

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.FragmentProfileBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserDataModel
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.about.AboutActivity
import org.obesifix.obesifix.ui.edit.EditActivity
import org.obesifix.obesifix.ui.home.HomeViewModel
import org.obesifix.obesifix.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userPreference = UserPreference.getInstance(requireContext().dataStore)
        val application = requireContext().applicationContext as Application
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext(), UserPreference.getInstance(requireContext().dataStore), application)
        )[ProfileViewModel::class.java]

        lifecycleScope.launch {
            val userDataModel: UserDataModel = userPreference.getUserData().first()

            userDataModel.let {
                Glide.with(this@ProfileFragment) // Make sure to replace with your actual fragment or activity reference
                    .load(it.picture)
                    .transform(CircleCrop())
                    .into(binding.profileImg)
            }

            binding.tvUsername.text = userDataModel.name
            binding.tvEmail.text = userDataModel.email
        }
        binding.logout.setOnClickListener {
            logoutUser()
        }

        binding.About.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        binding.MyProfile.setOnClickListener{
            val intent = Intent(requireContext(), EditActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun logoutUser() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Are you sure you want to logout?")

        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                val token: String = userPreference.getAccessTokenUser().first()
                Log.d(ContentValues.TAG, "token profile: $token")
                viewModel.requestLogout(token = token)
                viewModel.isNavigate.observe(viewLifecycleOwner) { shouldNavigate ->
                    if (shouldNavigate) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                userPreference.logout()
                            }
                        }
                        startActivity(Intent(context, LoginActivity::class.java))
                        requireActivity().finish()
                        dialogInterface.dismiss()
                    }
                }
            }
        }

        alertDialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
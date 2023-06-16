package org.obesifix.obesifix.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.FragmentProfileBinding
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.about.AboutActivity
import org.obesifix.obesifix.ui.edit.EditActivity
import org.obesifix.obesifix.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
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
        auth = FirebaseAuth.getInstance()

        val user: FirebaseUser? = auth.currentUser
        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val profileImageUrl: String? = user?.photoUrl?.toString()
        profileImageUrl?.let {
            Glide.with(this)
                .load(it)
                .transform(CircleCrop())
                .into(binding.profileImg)
        }

        val userName: String? = auth.currentUser?.displayName
        binding.tvUsername.text = userName

        val email: String? = auth.currentUser?.email
        binding.tvEmail.text = email

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
        // Build the alert dialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Are you sure you want to logout?")

        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
            // User clicked "Yes," perform logout
            auth.signOut()
            googleSignInClient.signOut()
            viewLifecycleOwner.lifecycleScope.launch {
                // Perform logout within a coroutine
                withContext(Dispatchers.IO) {
                    userPreference.logout()
                }
            }

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
            // User clicked "No," dismiss the dialog
            dialogInterface.dismiss()
        }

        // Show the alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
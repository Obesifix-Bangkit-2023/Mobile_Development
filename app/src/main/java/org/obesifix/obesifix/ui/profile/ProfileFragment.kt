package org.obesifix.obesifix.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.obesifix.obesifix.databinding.FragmentProfileBinding
import org.obesifix.obesifix.ui.about.AboutActivity
import org.obesifix.obesifix.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        val user: FirebaseUser? = auth.currentUser
        val profileImageUrl: String? = user?.photoUrl?.toString()
        profileImageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.profileImg)
        }

        val userName: String? = auth.currentUser?.displayName
        binding.tvUsername.text = userName

        val email: String? = auth.currentUser?.email
        binding.tvEmail.text = email

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        binding.About.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun logoutUser() {
        auth.signOut()
        
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}